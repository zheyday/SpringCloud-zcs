package zcs.oauthclient1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

//@EnableResourceServer
@SpringBootApplication
public class OauthClient1Application {

    public static void main(String[] args) {
        SpringApplication.run(OauthClient1Application.class, args);
    }

}
