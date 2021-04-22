package zcs.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zcs.commons.utils.CommonUtil;
import zcs.commons.utils.RedisUtil;
import zcs.commons.utils.ResponseState;
import zcs.seckill.model.Orders;
import zcs.seckill.service.impl.OrdersServiceImpl;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zcs
 * @since 2019-12-12
 */
@RestController
@RequestMapping("/order")
public class OrdersController {
    private final String SUCCESS = "success";
    private final OrdersServiceImpl ordersService;
    private final CommonUtil commonUtil;
    private final RedisUtil redisUtil;

    @Autowired
    public OrdersController(OrdersServiceImpl ordersService, CommonUtil commonUtil, RedisUtil redisUtil) {
        this.ordersService = ordersService;
        this.commonUtil = commonUtil;
        this.redisUtil = redisUtil;
    }

    /**
     * 获取订单列表
     * @param userId
     * @return
     */
    @GetMapping("/orders")
    public String getOrderList(@RequestParam("userId") String userId) {
        List<Orders> list = ordersService.getByCols("user_id",Integer.valueOf(userId));
        return commonUtil.toJson(ResponseState.OK, list);
    }

    /**
     * 下单 更新订单状态为已付款
     * @param orderId
     * @return
     */
    @PatchMapping("/orders")
    public String  pay(@RequestParam("orderId") Integer orderId) {
        Orders order = new Orders();
        order.setState(1);
        order.setId(orderId);
        ordersService.updateById(order);
//        WebSocketController.sendInfo("0","666");
        return commonUtil.toJson(ResponseState.OK);
    }

    @DeleteMapping("/orders")
    public String deleteOrder(@RequestParam("orderId")Integer id) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("good_id","user_id","state");
        queryWrapper.eq("id",id);
        Orders order = ordersService.getOne(queryWrapper);
        ordersService.removeById(id);
        if (order.getState()==0){
            String goodId = String.valueOf(order.getGoodId());
            redisUtil.set(goodId,(Integer) redisUtil.get(goodId) +1);
            redisUtil.hDelete(SUCCESS,order.getUserId()+ goodId);
        }
        return commonUtil.toJson(ResponseState.OK);
    }

    @RabbitListener(queues = "seckill")
    public void payMq(String msg) {
        Gson gson = new Gson();
        Orders order = gson.fromJson(msg, Orders.class);
        ordersService.save(order);
    }
}
