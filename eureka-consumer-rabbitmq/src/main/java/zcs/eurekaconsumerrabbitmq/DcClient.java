package zcs.eurekaconsumerrabbitmq;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//指定接口所要调用的服务名称
@FeignClient(name = "eureka-client",fallback = DcHystrix.class)
public interface DcClient {
    @GetMapping("/hi")
    String consumer(@RequestParam("name") String name);
}
