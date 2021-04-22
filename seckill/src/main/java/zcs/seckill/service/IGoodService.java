package zcs.seckill.service;

import zcs.seckill.model.Good;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 秒杀库存表 服务类
 * </p>
 *
 * @author zcs
 * @since 2020-07-11
 */
public interface IGoodService extends IService<Good> {
    Integer decreaseCount(Integer id);

    List<Good> selectSpecifiedCols(String... cols);
}
