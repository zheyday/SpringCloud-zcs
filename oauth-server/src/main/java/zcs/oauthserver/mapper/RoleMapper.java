package zcs.oauthserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import zcs.oauthserver.model.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zcs
 * @since 2019-10-09
 */
@Component
public interface RoleMapper extends BaseMapper<Role> {
   @Select("select name from role r INNER JOIN user_role ur on ur.user_id=#{userId} and ur.role_id=r.id")
    List<Role> selectByUserId(Integer userId);
}
