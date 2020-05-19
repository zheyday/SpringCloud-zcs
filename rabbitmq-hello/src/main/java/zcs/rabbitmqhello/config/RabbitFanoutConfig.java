package zcs.rabbitmqhello.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Fanout模式
 */
@Configuration
public class RabbitFanoutConfig {
    @Bean
    public Queue aMessage(){
        return new Queue("qa_fanout");
    }

    @Bean
    public Queue bMessage(){
        return new Queue("qb_fanout");
    }
    /**
     * 声明一个fanout类型的交换机
     * @return
     */
    @Bean
    FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanoutExchange");
    }

    /**
     * a队列订阅fanout交换机
     * @param aMessage
     * @param fanoutExchange
     * @return
     */
    @Bean
    Binding bindingExchangeA(Queue aMessage, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(aMessage).to(fanoutExchange);
    }

    /**
     * b队列订阅fanout交换机
     * @param bMessage
     * @param fanoutExchange
     * @return
     */
    @Bean
    Binding bindingExchangeB(Queue bMessage, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(bMessage).to(fanoutExchange);
    }
}
