package zcs.rabbitmqhello;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Sender {
    private final AmqpTemplate rabbitTemplate;

    @Autowired
    public Sender(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

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
        rabbitTemplate.convertAndSend("topicExchange","topic.message", context);
    }

    //会匹配到topic.#  只有Receiver2可以收到消息
    public void send2() throws Exception {
        String context = "hello rabbitAmqp" + new Date();
        System.out.println("Sender: " + context);
        rabbitTemplate.convertAndSend("topicExchange","topic.messages", context);
    }

    public void send3(){
        String context = "hello rabbitAmqp" + new Date();
        System.out.println("Sender: " + context);
        rabbitTemplate.convertAndSend("fanoutExchange","", context);
    }
}
