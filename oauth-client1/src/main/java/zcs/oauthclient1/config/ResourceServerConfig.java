package zcs.oauthclient1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 优先级默认是3
 * 比WebSecurityConfig大
 * 如果拦截的url和WebSecurityConfigurerAdapter配置的重合 那么只有这个会生效
 */
//@Configuration
//@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
//                antMatcher表示只能处理/user的请求
                .antMatcher("/user/**")
                .authorizeRequests()
                .antMatchers("/user/test1").permitAll()
                .antMatchers("/user/test2").authenticated()
//                .antMatchers("user/test2").hasRole("USER")
//                .anyRequest().authenticated()
        ;
    }
}