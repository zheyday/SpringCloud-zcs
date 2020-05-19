package zcs.user_center.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
//Spring Security默认禁用注解 这里开启注解
// 结合@PreAuthorize("hasRole('admin')")用来判断用户对某个控制层的方法是否有访问权限
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
//                配置需要权限的url
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().csrf().disable();
        ;
    }
}
