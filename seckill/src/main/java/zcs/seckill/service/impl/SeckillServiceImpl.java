package zcs.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zcs.seckill.mapper.SeckillMapper;
import zcs.seckill.model.Seckill;
import zcs.seckill.service.ISeckillService;

import java.util.List;

/**
 * <p>
 * 秒杀库存表 服务实现类
 * </p>
 *
 * @author zcs
 * @since 2019-11-08
 */
@Service
public class SeckillServiceImpl extends ServiceImpl<SeckillMapper, Seckill> implements ISeckillService {
    private final SeckillMapper seckillMapper;

    @Autowired
    public SeckillServiceImpl(SeckillMapper seckillMapper) {
        this.seckillMapper = seckillMapper;
    }

    @Override
    public Integer minusStack(Integer id) {
        return seckillMapper.minusOne();
    }

    @Override
    public List<Seckill> selectSpecifiedCols(String... cols) {
        QueryWrapper<Seckill> queryWrapper=new QueryWrapper<>();
        queryWrapper.select(cols);
        return seckillMapper.selectList(queryWrapper);
    }
}
