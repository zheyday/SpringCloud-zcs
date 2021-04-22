package zcs.rabbitmqhello;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static zcs.rabbitmqhello.config.RabbitDelayConfig.DELAY_EXCHANGE_NAME;
import static zcs.rabbitmqhello.config.RabbitDelayConfig.DELAY_QUEUE_ROUTING_KEY;

@Component
public class Sender {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    final RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, b, s) -> {
        if (b) {
            System.out.println("success");
        }else
            System.out.println("error");
    };
    final RabbitTemplate.ReturnCallback returnCallback= (message, i, s, s1, s2) -> {

    };

    public void send() throws Exception {
        for (int i = 0; i < 20; i++) {
            String context = "hello rabbitAmqp" + new Date();
            System.out.println("Sender: " + context);
            rabbitTemplate.convertAndSend("hello", context);
        }
    }

    //会匹配到topic.#和topic.message 两个Receiver都可以收到消息
    public void send1() throws Exception {
        String context = "hello rabbitAmqp" + new Date();
        System.out.println("Sender: " + context);
//        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.convertAndSend("topicExchange", "topic.message", context);
    }

    //会匹配到topic.#  只有Receiver2可以收到消息
    public void send2() throws Exception {
        String context = "hello rabbitAmqp" + new Date();
        System.out.println("Sender: " + context);
        rabbitTemplate.convertAndSend("topicExchange", "topic.messages", context);
    }

    public void send3() {
        String context = "hello rabbitAmqp" + new Date();
        System.out.println("Sender: " + context);
        rabbitTemplate.convertAndSend("fanoutExchange", "", context);
    }

    public void sendTTL(){
        String context = "hello rabbitAmqp" + new Date();
        System.out.println("Sender: " + context);
        rabbitTemplate.convertAndSend(DELAY_EXCHANGE_NAME, DELAY_QUEUE_ROUTING_KEY, context, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration(String.valueOf(5000));
                return message;
            }
        });
    }
}
