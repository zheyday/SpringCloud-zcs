package zcs.oauthserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zcs.oauthserver.model.User;

import java.security.Principal;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/info")
    public User getCurUser(Authentication authentication){
        return (User) authentication.getPrincipal();
    }
}
