package zcs.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import zcs.seckill.model.Seckill;

/**
 * <p>
 * 秒杀库存表 Mapper 接口
 * </p>
 *
 * @author zcs
 * @since 2019-11-08
 */
@Component
public interface SeckillMapper extends BaseMapper<Seckill> {
    @Update("update seckill set number=number-1 where id=1 and number > 0 ")
    Integer minusOne();
}
