package zcs.eurekaconsumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name = "user-center")
public interface UserCenterClient {
    @PostMapping("/login")
    String login(String name, String psd);
}
