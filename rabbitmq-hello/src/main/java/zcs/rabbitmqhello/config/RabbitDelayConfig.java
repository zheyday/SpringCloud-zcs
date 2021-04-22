package zcs.rabbitmqhello.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class RabbitDelayConfig {
    public static final String DELAY_QUEUE_ROUTING_KEY="delay.routing_key";
    public static final String DELAY_EXCHANGE_NAME="delayExchange";
    public static final String DLX_NAME="dlxExchange";
    public static final String DLQ_NAME="dlq";
    public static final String DLQ_10s_ROUTING_KEY="dlq.delay_10s";
    @Bean
    public DirectExchange delayExchange(){
        return new DirectExchange(DELAY_EXCHANGE_NAME);
    }
//    延时队列 绑定到死信交换机
    @Bean
    public Queue delayQueue() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", DLX_NAME);
        params.put("x-dead-letter-routing-key",DLQ_10s_ROUTING_KEY);
        return new Queue("delayQueueA",true,false,false,params);
    }

    @Bean
    public Binding bindingDelay(Queue delayQueue,DirectExchange delayExchange){
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAY_QUEUE_ROUTING_KEY);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_NAME);
    }

    @Bean
    public Queue dlq() {
        return new Queue(DLQ_NAME);
    }

    @Bean
    public Binding bindingDeadLetter(Queue dlq, DirectExchange dlxExchange) {
        return BindingBuilder.bind(dlq).to(dlxExchange).with(DLQ_10s_ROUTING_KEY);
    }
}
