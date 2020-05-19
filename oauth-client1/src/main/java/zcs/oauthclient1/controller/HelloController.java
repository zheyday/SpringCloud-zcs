package zcs.oauthclient1.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName() + Arrays.toString(authentication.getAuthorities().toArray());
    }

    @GetMapping("/current")
    public Principal user(Principal principal) {
        return principal;
    }

    @GetMapping("/user/test1")
    public String test() {
        return "test1";
    }

    @GetMapping("/user/test2")
    public String test2() {
        return "test2";
    }
}
