package zcs.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import zcs.seckill.model.Orders;
import zcs.seckill.mapper.OrdersMapper;
import zcs.seckill.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zcs
 * @since 2020-07-11
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {
    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public List<Orders> getByCols(String cols, Object value) {
        QueryWrapper<Orders> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq(cols,value);
        return ordersMapper.selectList(orderQueryWrapper);
    }
}
