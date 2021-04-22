package zcs.seckill.service;

import zcs.seckill.model.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zcs
 * @since 2020-07-11
 */
public interface IOrdersService extends IService<Orders> {
    public List<Orders> getByCols(String cols, Object value);
}
