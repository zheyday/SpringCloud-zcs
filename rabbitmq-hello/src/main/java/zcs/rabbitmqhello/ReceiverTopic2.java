package zcs.rabbitmqhello;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ReceiverTopic2 {
    @RabbitListener(queues = "messages")
    public void process(Message mes, Channel channel) throws IOException {
        channel.basicAck(mes.getMessageProperties().getDeliveryTag(),true);
        System.out.println("ReceiverTopic2:" + new String(mes.getBody()));
    }
}
