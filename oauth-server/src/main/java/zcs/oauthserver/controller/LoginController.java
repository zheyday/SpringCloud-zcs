package zcs.oauthserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import zcs.commons.utils.CommonUtil;
import zcs.commons.utils.ResponseState;
import zcs.oauthserver.model.User;
import zcs.oauthserver.service.IUserService;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Map;

//@CrossOrigin
@RestController
public class LoginController {
    @Autowired
    private RestTemplate restTemplate;
    private final IUserService userService;
    private final CommonUtil commonUtil;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }

    public LoginController(IUserService userService, CommonUtil commonUtil) {
        this.userService = userService;
        this.commonUtil = commonUtil;
    }

    @GetMapping("/loginp")
    public void hello(HttpServletResponse response) throws Exception {
        String redirectUrl = "http://106.14.45.78:9120/token";
        String url = "http://106.14.45.78:9120/oauth/authorize?client_id=zuul&response_type=code&redirect_uri=" + redirectUrl;
        response.sendRedirect(url);
    }

    @GetMapping("/token")
    public void token(String code, HttpServletResponse response) throws Exception {
        if (code != null) {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("code", code);
            map.add("client_id", "zuul");
            map.add("client_secret", "zuul");
            map.add("redirect_uri", "http://106.14.45.78:9120/token");
            map.add("grant_type", "authorization_code");
            Map<String, String> resp = restTemplate.postForObject("http://106.14.45.78:9120/oauth/token", map, Map.class);
            String access_token = resp.get("access_token");
            response.sendRedirect("http://106.14.45.78?access_token=" + access_token);
        }
    }

    @GetMapping("/current")
    public Principal user(Principal principal) {
        return principal;
    }

    @GetMapping("/info")
    public String getInfo(Principal principal) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", principal.getName());
        User user = userService.getOne(userQueryWrapper);
        user.setPassword("");
        return commonUtil.toJson(ResponseState.OK, user);
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String userName, @RequestParam("password") String passWord, String phone, String email) {
        User user = new User(userName, passWord, phone, email);
        try {
            userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                return commonUtil.toJson(ResponseState.DUPLICATE);
            }
            return commonUtil.toJson(ResponseState.FAIL);
        }
        return commonUtil.success();
    }

}
