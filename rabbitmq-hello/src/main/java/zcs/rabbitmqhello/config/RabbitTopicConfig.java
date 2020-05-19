package zcs.rabbitmqhello.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * topic模式
 */
@Configuration
public class RabbitTopicConfig {

    @Bean
    public Queue queueMessage(){
        return new Queue("message");
    }

    @Bean
    public Queue queueMessages(){
        return new Queue("messages");
    }
    /**
     * 声明一个topic类型的交换机
     * @return
     */
    @Bean
    TopicExchange exchange(){
        return new TopicExchange("topicExchange");
    }

    /**
     * 绑定队列到交换机
     * 接收key为topic.message的消息
     * @param queueMessage
     * @param topicExchange
     * @return
     */
    @Bean
    Binding bindingMessage(Queue queueMessage,TopicExchange topicExchange){
        return BindingBuilder.bind(queueMessage).to(topicExchange).with("topic.message");
    }

    /**
     * 绑定队列到交换机
     * 接收key为topic.#的消息
     * @param queueMessages
     * @param topicExchange
     * @return
     */
    @Bean
    Binding bindingMessages(Queue queueMessages,TopicExchange topicExchange){
        return BindingBuilder.bind(queueMessages).to(topicExchange).with("topic.#");
    }
}
