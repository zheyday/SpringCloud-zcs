package zcs.rabbitmqhello;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver2 {
    @RabbitListener(queues = "hello")
    public void process(String mes){
        System.out.println("Receiver2: " + mes);
    }
}
