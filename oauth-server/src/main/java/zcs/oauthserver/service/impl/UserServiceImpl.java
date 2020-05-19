package zcs.oauthserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zcs.oauthserver.mapper.UserMapper;
import zcs.oauthserver.model.User;
import zcs.oauthserver.service.IUserService;

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
