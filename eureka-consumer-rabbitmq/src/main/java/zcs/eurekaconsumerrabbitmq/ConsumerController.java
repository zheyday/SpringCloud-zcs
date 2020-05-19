package zcs.eurekaconsumerrabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {
    @Autowired
    private DcClient dcClient;

    @GetMapping("/hi")
    public String dc( String name){
        return dcClient.consumer(name);
    }
}
