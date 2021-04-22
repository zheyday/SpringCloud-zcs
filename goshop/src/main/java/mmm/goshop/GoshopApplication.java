package mmm.goshop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
@MapperScan("mmm.goshop.mapper")
public class GoshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoshopApplication.class, args);
    }

}
