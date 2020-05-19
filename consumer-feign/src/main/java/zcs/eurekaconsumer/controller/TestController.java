package zcs.eurekaconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import zcs.eurekaconsumer.DcClient;

@RestController
public class TestController {
    @Autowired
    private DcClient dcClient;
    @GetMapping("/hi")
    public String test(){
        return dcClient.consumer("zcs");
    }
}
