package zcs.seckill.mapper;

import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import zcs.seckill.model.Good;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 秒杀库存表 Mapper 接口
 * </p>
 *
 * @author zcs
 * @since 2020-07-11
 */
@Component
public interface GoodMapper extends BaseMapper<Good> {
    @Update("update good set number=number-1 where id=1 and number > 0 ")
    Integer minusOne();
}
