package zcs.rabbitmqhello;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "message")
public class ReceiverTopic {
    @RabbitHandler
    public void process(String message) {
        System.out.println("ReceiverTopic: " + message);
    }

}
