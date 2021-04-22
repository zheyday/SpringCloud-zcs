package zcs.seckill.controller;


import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zcs.commons.utils.CommonUtil;
import zcs.commons.utils.RedisUtil;
import zcs.commons.utils.ResponseState;
import zcs.seckill.model.Good;
import zcs.seckill.model.Orders;
import zcs.seckill.service.impl.GoodServiceImpl;
import zcs.seckill.utils.ZkUtils;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 秒杀库存表 前端控制器
 * </p>
 *
 * @author zcs
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/good")
@SuppressWarnings("UnstableApiUsage")
public class GoodController {
    private final GoodServiceImpl goodService;
    private final CommonUtil commonUtil;
    private final AmqpTemplate amqpTemplate;
    private ZkUtils zkUtils;

    private final static Gson gson = new Gson();
    private final RedisUtil redisUtil;
    private final String SUCCESS = "success";

    private Map<String, Boolean> localOverMap = new HashMap<>();
    private Map<String, String> idNameMap = new HashMap<>();

    private RateLimiter rateLimiter = RateLimiter.create(10);

    @Autowired
    public GoodController(GoodServiceImpl goodService, CommonUtil commonUtil, AmqpTemplate amqpTemplate,  RedisUtil redisUtil) {
        this.goodService = goodService;
        this.commonUtil = commonUtil;
        this.amqpTemplate = amqpTemplate;
        this.redisUtil = redisUtil;
        initSecKillInfo();
    }

    //    把数据库中的商品加载到redis
    private void initSecKillInfo() {
        List<Good> list = goodService.selectSpecifiedCols("id", "username", "number");

        list.forEach(item -> {
            redisUtil.set(String.valueOf(item.getId()), goodService.getById(item.getId()).getNumber());
            localOverMap.put(String.valueOf(item.getId()), false);
            idNameMap.put(String.valueOf(item.getId()), item.getUsername());
        });
    }

    @GetMapping("/goods")
    public String getAllSecKill() {
        List<Good> list = goodService.list();
        for (Good good : list) {
            good.setNumber((Integer) redisUtil.get(String.valueOf(good.getId())));
        }
        return commonUtil.toJson(ResponseState.OK, list);
    }

    @PostMapping("/order")
    public String reduceStack(@PathParam("id") String id) {
        Integer number;
        synchronized (this) {
            Good seckill = goodService.getById(id);
            number = seckill.getNumber();

            if (number > 0) {
                seckill.setNumber(--number);
                goodService.updateById(seckill);
                number = goodService.getById(id).getNumber();
            }
        }
        return commonUtil.toJson(ResponseState.OK, "number", number);
    }

    @PostMapping("/orderSql")
    public String reduceStack(@PathParam("id") Integer id) {
        Integer number = -1;
        if (goodService.decreaseCount(id) != 0) {
            number = goodService.getById(id).getNumber();
        }
        return commonUtil.toJson(ResponseState.OK, "number", number);
    }

    @PostMapping("/orderRedis")
    public String reduceStackRedis(@PathParam("goodId") String goodId, @PathParam("userId") String userId) {
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

    //    @ServiceLimit
    @PostMapping("/orderMq")
    public String reduceStackMq(@RequestParam("goodId") String goodId, @RequestParam("userId") String userId) {
        String componentKey = userId + goodId;

//        if (localOverMap.get(goodId)) {
//            int number = redisUtil.hGet(prefix + SUCCESS, componentKey) == null ? -1 : -2;
//            return commonUtil.toJson(ResponseState.OK, "number", number);
//        }
        Long number = redisUtil.lua(goodId, goodId + SUCCESS, componentKey);
//        Long number = redisUtil.handleStock(goodId, goodId + SUCCESS, componentKey);

//        zk实现
//        String lock = "/lock" + goodId;
//        String nodeName = zkUtils.lock(lock);
//        zkUtils.unLock(lock, nodeName);
        if (number >= 0) {
//            成功
//            放入redis 用于查重
            redisUtil.hPut(goodId + SUCCESS, componentKey, 1);
            Orders order = new Orders();
            order.setUserId(Integer.valueOf(userId));
            order.setGoodId(Integer.valueOf(goodId));
            order.setGoodName(idNameMap.get(goodId));
            order.setState(0);
            amqpTemplate.convertAndSend("seckill", gson.toJson(order));
        }
        return commonUtil.success("number", number);
    }
}
