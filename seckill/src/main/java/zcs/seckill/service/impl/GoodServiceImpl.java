package zcs.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import zcs.seckill.model.Good;
import zcs.seckill.mapper.GoodMapper;
import zcs.seckill.service.IGoodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 秒杀库存表 服务实现类
 * </p>
 *
 * @author zcs
 * @since 2020-07-11
 */
@Service
public class GoodServiceImpl extends ServiceImpl<GoodMapper, Good> implements IGoodService {
    private final GoodMapper goodMapper;

    @Autowired
    public GoodServiceImpl(GoodMapper goodMapper) {
        this.goodMapper = goodMapper;
    }
    @Override
    public Integer decreaseCount(Integer id) {
        return goodMapper.minusOne();
    }

    @Override
    public List<Good> selectSpecifiedCols(String... cols) {
        QueryWrapper<Good> queryWrapper=new QueryWrapper<>();
        queryWrapper.select(cols);
        return goodMapper.selectList(queryWrapper);
    }
}
