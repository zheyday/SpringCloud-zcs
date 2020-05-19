package zcs.rabbitmqhello;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "qa_fanout")
public class ReceiverFanout {
    @RabbitHandler
    public void process(String mes){
        System.out.println("ReceiverFanout: " + mes);
    }
}
