package zcs.rabbitmqhello;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ReceiverTopic {
    @RabbitListener(queues = "message")
    public void process(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        System.out.println("ReceiverTopic: " + new String(message.getBody()));
    }
}
