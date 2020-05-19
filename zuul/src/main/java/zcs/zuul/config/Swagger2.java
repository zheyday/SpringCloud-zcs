package zcs.zuul.config;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@EnableSwagger2Doc
@Component
@Primary
public class Swagger2 implements SwaggerResourcesProvider {
    final ZuulProperties zuulProperties;

    public Swagger2(ZuulProperties zuulProperties) {
        this.zuulProperties = zuulProperties;
    }

    @Override
    public List<SwaggerResource> get() {
        Set<String> routeList=zuulProperties.getRoutes().keySet();
        List<SwaggerResource> resources = new ArrayList<>();
        for (String s : routeList) {
            resources.add(swaggerResource(s,"/"+s+"/v2/api-docs","2.0"));
        }
        return resources;
    }

    private SwaggerResource swaggerResource(String name,String location,String version){
        SwaggerResource swaggerResource=new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
