package zcs.orders.service.impl;

import zcs.orders.model.Orders;
import zcs.orders.mapper.OrdersMapper;
import zcs.orders.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zcs
 * @since 2021-04-19
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

}
