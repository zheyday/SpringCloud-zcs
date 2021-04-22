package zcs.user_center.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @ServiceLimit
    @GetMapping("/order")
    public String order(){
        return "11";
    }
}
