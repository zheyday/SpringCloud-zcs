package zcs.rabbitmqhello;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    @RabbitListener(queues = "hello")
    public void process(String mes) {
        System.out.println("Receiver: " + mes);
    }
}
