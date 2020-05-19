package zcs.feignconsumer;

import feign.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    DcClient dcClient;
    @GetMapping("hi")
    public String test(){
        return dcClient.consumer("zcs");
    }
}
