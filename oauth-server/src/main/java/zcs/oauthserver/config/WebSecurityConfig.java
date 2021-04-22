package zcs.oauthserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import zcs.commons.utils.CommonUtil;
import zcs.oauthserver.service.UserServiceDetail;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserServiceDetail userServiceDetail;

    private final CommonUtil commonUtil;

    @Autowired
    public WebSecurityConfig(CommonUtil commonUtil) {
        this.commonUtil = commonUtil;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Bean
//    RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        roleHierarchy.setHierarchy("ROLE_admin>ROLE_user");
//        return roleHierarchy;
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceDetail).passwordEncoder(passwordEncoder());
    }


    @Override
    public void configure(WebSecurity web){
        web.ignoring().antMatchers("/*.ico","/loginp","/register","/token","/bootstrap/**","/login.html");
    }

    /**
     * 配置了默认表单登陆以及禁用了 csrf 功能，并开启了httpBasic 认证
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                拦截的url
                .requestMatchers()
                .antMatchers("/oauth/authorize","/login")
                .and()
//                权限验证相关
                .authorizeRequests()
//                .antMatchers("/admin/**").hasAuthority("admin")
//                .antMatchers("/oauth2_token/**").hasAuthority("admin")
                .anyRequest().authenticated()
                .and()
                // 配置登陆页/login并允许访问
                .formLogin()
//                自定义登陆页面和url 并且要放开访问权限
                .loginPage("/login.html")
//                如果没有指定登陆接口 默认提交地址就是login.html
                .loginProcessingUrl("/login")
//                自定义登陆成功事件
//                .successHandler((request, response, authentication) -> {
//                    response.setContentType("application/json;charset=utf-8");
//                    PrintWriter out = response.getWriter();
//                    User user = (User) authentication.getPrincipal();
//                    user.setPassword("");
//                    out.write(commonUtil.toJson(ResponseState.OK, user));
//                    out.flush();
//                    out.close();
//                    response.sendRedirect("http://localhost:9528/#/dashboard?islogin=true&id="+user.getId() );
//                })
                .permitAll()
                // 登出页
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll()
//                放开自定义登陆访问权限
//                .antMatchers("/login").permitAll()
                // 由于使用的是JWT，我们这里不需要csrf
                .and().csrf().disable()
//                .sessionManagement()                        // 定制我们自己的 session 策略
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
    }
}

