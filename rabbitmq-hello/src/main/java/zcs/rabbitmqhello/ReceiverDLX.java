package zcs.rabbitmqhello;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

import static zcs.rabbitmqhello.config.RabbitDelayConfig.DLQ_NAME;

@Component
public class ReceiverDLX {
    @RabbitListener(queues=DLQ_NAME)
    public void process(Message message, Channel channel) throws IOException {
        System.out.println(new Date()+"receiver:"+new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
    }
}
