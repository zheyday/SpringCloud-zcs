package zcs.seckill.controller;


import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zcs.commons.utils.CommonUtil;
import zcs.commons.utils.RedisUtil;
import zcs.commons.utils.ResponseState;
import zcs.seckill.model.Seckill;
import zcs.seckill.model.SuccessKilled;
import zcs.seckill.service.impl.SeckillServiceImpl;
import zcs.seckill.service.impl.SuccessKilledServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 秒杀库存表 前端控制器
 * </p>
 *
 * @author zcs
 * @since 2019-11-06
 */
@RestController
@SuppressWarnings("UnstableApiUsage")
public class SeckillController {
    private final SeckillServiceImpl seckillService;
    private final SuccessKilledServiceImpl successKilledService;
    private final CommonUtil commonUtil;
    private final AmqpTemplate amqpTemplate;

    private final RedisUtil redisUtil;
    private final String SUCCESS = "success";

    private Map<String, Boolean> localOverMap = new HashMap<>();
    private Map<String, String> idNameMap = new HashMap<>();

    private RateLimiter rateLimiter = RateLimiter.create(10);

    @Autowired
    public SeckillController(SeckillServiceImpl seckillService, SuccessKilledServiceImpl successKilledService, CommonUtil commonUtil, AmqpTemplate amqpTemplate, RedisUtil redisUtil) {
        this.seckillService = seckillService;
        this.successKilledService = successKilledService;
        this.commonUtil = commonUtil;
        this.amqpTemplate = amqpTemplate;
        this.redisUtil = redisUtil;
        localOverMap.put("1", false);
        initSecKillInfo();
    }

    //    把数据库中的商品加载到redis
    private void initSecKillInfo() {
        List<Seckill> list = seckillService.selectSpecifiedCols("id", "username", "number");
        list.forEach(item -> {
            redisUtil.set(String.valueOf(item.getId()), seckillService.getById(item.getId()).getNumber());
            localOverMap.put(String.valueOf(item.getId()), false);
            idNameMap.put(String.valueOf(item.getId()), item.getUsername());
        });
    }

    @GetMapping("/list")
    public String getAllSecKill() {
        List<Seckill> list = seckillService.list();
        return commonUtil.toJson(ResponseState.OK, list);
//        return JSON.toJSONString(resultData);
    }

    @GetMapping("/order")
    public String reduceStack(@RequestParam("id") String id) {
        Integer number;
        synchronized (this) {
            Seckill seckill = seckillService.getById(id);
            number = seckill.getNumber();

            if (number > 0) {
                seckill.setNumber(--number);
                seckillService.updateById(seckill);
                number = seckillService.getById(id).getNumber();
            }
        }
        return commonUtil.toJson(ResponseState.OK, "number", number);
    }

    @GetMapping("/orderSql")
    public String reduceStack(@RequestParam("id") Integer id) {
        Integer number = -1;
        if (seckillService.minusStack(id) != 0) {
            number = seckillService.getById(id).getNumber();
        }
        return commonUtil.toJson(ResponseState.OK, "number", number);
    }

    @GetMapping("/orderRedis")
    public String reduceStackRedis(@RequestParam("goodId") String goodId, @RequestParam("userId") String userId) {
//        分布式锁
//        if (!redisUtil.setnx("lock", 1, 1))
//            return "so busy";
//        Integer number = (Integer) redisUtil.get(KEY);
//        if (number > 0) {
//            number = redisUtil.decrBy(KEY, 1);
//        }
//        redisUtil.unlock("lock");

//        乐观锁
        if (redisUtil.watch(goodId)) {
//            如果秒杀成功 记录信息
            redisUtil.hPut(SUCCESS, userId, goodId);
        }
        return commonUtil.toJson(ResponseState.OK, "number", redisUtil.get(goodId));
    }

    @GetMapping("/orderlua")
    public String reduceStackLua(@RequestParam("goodId") String goodId, @RequestParam("userId") String userId) {
        //判断能否在1秒内得到令牌，如果不能则立即返回false，不会阻塞程序
        if (!rateLimiter.tryAcquire(1, TimeUnit.SECONDS))
            return commonUtil.toJson(ResponseState.OK, "number", -1);
        Long number = redisUtil.lua(goodId, SUCCESS, userId);
        if (number >= 0) {
//            成功
            redisUtil.hPut(SUCCESS, userId, goodId);
        }
        return commonUtil.toJson(ResponseState.OK, "number", number);
    }

    @GetMapping("/orderMq")
    public String reduceStackMq(@RequestParam("goodId") String goodId, @RequestParam("userId") String userId) {
        String componentKey = userId + goodId;
        if (localOverMap.get(goodId)) {
            int number = redisUtil.hGet(SUCCESS, componentKey) == null ? -1 : -2;
            return commonUtil.toJson(ResponseState.OK, "number", number);
        }

        Long number = redisUtil.lua(goodId, SUCCESS, componentKey);
        if (number >= 0) {
//            成功
//            放入redis 用于查重
            redisUtil.hPut(SUCCESS, componentKey, 1);

            SuccessKilled successKilled = new SuccessKilled();
            successKilled.setUserId(Integer.valueOf(userId));
            successKilled.setState(0);
            successKilled.setGoodName(idNameMap.get(goodId));
            Gson gson = new Gson();
            amqpTemplate.convertAndSend("seckill", gson.toJson(successKilled));
        } else
            localOverMap.put(goodId, true);
        return commonUtil.toJson(ResponseState.OK, "number", number);
    }


    @GetMapping("/pay")
    public String pay(@RequestParam("orderId") Integer orderId) {
        SuccessKilled successKilled = new SuccessKilled();
        successKilled.setState(1);
        successKilled.setId(orderId);
        successKilledService.updateById(successKilled);
        return commonUtil.toJson(ResponseState.OK);
    }

    @GetMapping("/orderList")
    public String getOrderList(@RequestParam("userId") Integer userId) {
        List<SuccessKilled> list = successKilledService.list();
        return commonUtil.toJson(ResponseState.OK, list);
    }

    @RabbitListener(queues = "seckill")
    public void payMq(String msg) {
        Gson gson = new Gson();
        SuccessKilled successKilled = gson.fromJson(msg, SuccessKilled.class);
        successKilledService.save(successKilled);
    }

}
