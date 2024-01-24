package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@EnableRabbit
@Configuration
public class RabbitMqConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port:5672}")
    private int port;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);

        log.info("Creating connection factory with: %s @ %s : %s / %s".formatted(username, host, port, virtualHost));

        return connectionFactory;
    }

    /**
     * Required for executing adminstration functions against an AMQP Broker
     */
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

//    /**
//     * This queue will be declared. This means it will be created if it does not exist. Once declared, you can do something
//     * like the following:
//     *
//     * @RabbitListener(queues = "#{@myDurableQueue}")
//     * @Transactional
//     * public void handleMyDurableQueueMessage(CustomDurableDto myMessage) {
//     *    // Anything you want! This can also return a non-void which will queue it back in to the queue attached to @RabbitListener
//     * }
//     */
//    @Bean
//    public Queue myDurableQueue() {
//        return new Queue(queue, true, false, false);
//    }
//
//    /**
//     * The following is a complete declaration of an exchange, a queue and a exchange-queue binding
//     */
////    @Bean
//    public TopicExchange createExchange() {
//        var topic = new TopicExchange(exchange, true, false);
//        topic.setShouldDeclare(false);
//
//        return topic;
//    }
//
//    @Bean
//    public Binding exchangeBinding() {
//        // Important part is the routing key -- this is just an example
//        return BindingBuilder.bind(myDurableQueue()).to(createExchange()).with(routingKey);
//    }
}
