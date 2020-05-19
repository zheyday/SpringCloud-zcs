package zcs.rabbitmqhello;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "qb_fanout")
public class ReceiverFanout2 {
    @RabbitHandler
    public void process(String mes){
        System.out.println("ReceiverFanout2: " + mes);
    }
}
