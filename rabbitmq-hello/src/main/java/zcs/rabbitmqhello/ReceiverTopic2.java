package zcs.rabbitmqhello;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "messages")
public class ReceiverTopic2 {
    @RabbitHandler
    public void process(String mes) {
        System.out.println("ReceiverTopic2:" + mes);
    }
}
