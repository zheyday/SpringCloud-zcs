package zcs.user_center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zcs.user_center.mapper.UserMapper;
import zcs.user_center.model.User;
import zcs.user_center.service.IUserService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zcs
 * @since 2019-11-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
