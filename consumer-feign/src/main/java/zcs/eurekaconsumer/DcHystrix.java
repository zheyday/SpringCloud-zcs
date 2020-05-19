package zcs.eurekaconsumer;

import org.springframework.stereotype.Component;

@Component
public class DcHystrix implements DcClient {
    @Override
    public String consumer(String name) {
        return "error: this is consumer-feign-hystrix";
    }
}
