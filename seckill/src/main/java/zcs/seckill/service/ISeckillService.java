package zcs.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import zcs.seckill.model.Seckill;

import java.util.List;

/**
 * <p>
 * 秒杀库存表 服务类
 * </p>
 *
 * @author zcs
 * @since 2019-11-08
 */
public interface ISeckillService extends IService<Seckill> {
    Integer minusStack(Integer id);

    List<Seckill> selectSpecifiedCols(String... cols);
}
