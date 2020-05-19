package zcs.seckill.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zcs.commons.utils.CommonUtil;
import zcs.commons.utils.ResponseState;
import zcs.seckill.model.SuccessKilled;
import zcs.seckill.service.impl.SuccessKilledServiceImpl;

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
@RequestMapping("/successKilled")
public class SuccessKilledController {
    private final SuccessKilledServiceImpl successKilledService;
    private final CommonUtil commonUtil;

    @Autowired
    public SuccessKilledController(SuccessKilledServiceImpl successKilledService, CommonUtil commonUtil) {
        this.successKilledService = successKilledService;
        this.commonUtil = commonUtil;
    }

    @GetMapping("/orderList")
    public String getOrderList(@RequestParam("userId") String userId) {
        List<SuccessKilled> list = successKilledService.list();
        return commonUtil.toJson(ResponseState.OK, list);
    }

    @DeleteMapping("/order")
    public String deleteOrder(@RequestParam("orderId")Integer id) {
        successKilledService.removeById(id);
        return commonUtil.toJson(ResponseState.OK);
    }
}
