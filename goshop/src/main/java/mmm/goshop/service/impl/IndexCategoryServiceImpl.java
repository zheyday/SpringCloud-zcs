package mmm.goshop.service.impl;

import mmm.goshop.mapper.IndexCategoryMapper;
import mmm.goshop.model.IndexCategory;
import mmm.goshop.service.IIndexCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class IndexCategoryServiceImpl extends ServiceImpl<IndexCategoryMapper, IndexCategory> implements IIndexCategoryService {
    private final IndexCategoryMapper indexCategoryMapper;

    @Autowired
    public IndexCategoryServiceImpl(IndexCategoryMapper indexCategoryMapper) {
        this.indexCategoryMapper = indexCategoryMapper;
    }
}
