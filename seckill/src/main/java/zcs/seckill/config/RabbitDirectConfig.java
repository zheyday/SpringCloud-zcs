package zcs.seckill.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitDirectConfig {
    @Bean
    public Queue seckillQueue(){
        return new Queue("seckill");
    }
}
