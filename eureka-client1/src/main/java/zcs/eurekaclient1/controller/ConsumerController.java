package zcs.eurekaclient1.controller;


import com.spring4all.swagger.EnableSwagger2Doc;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableSwagger2Doc
@RestController
public class ConsumerController {
    @Value("${server.port}")
    String port;

    @ApiOperation(value = "获取端口号",notes = ".")
    @ApiImplicitParam(name = "name",value = "用户名",required = true,dataType = "String")
    @GetMapping("/hi")
    public String dc(String name) {
        return "hello" + name + " i am from " + port;
    }
}
