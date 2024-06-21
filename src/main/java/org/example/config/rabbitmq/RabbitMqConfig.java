package org.example.config.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.MultiRabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactoryContextWrapper;
import org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Map;

@Slf4j
@Configuration
@Import({FirstVendorRabbitMqConfig.class, SecondVendorRabbitMqConfig.class})
public class RabbitMqConfig {
    @Bean
    SimpleRoutingConnectionFactory simpleRoutingConnectionFactory(@Qualifier("firstVendorConnectionFactory") CachingConnectionFactory firstVendorConnectionFactory,
                                                                  @Qualifier("secondVendorConnectionFactory") CachingConnectionFactory secondVendorConnectionFactory) {
        final var simpleRoutingConnectionFactory = new SimpleRoutingConnectionFactory();
        simpleRoutingConnectionFactory.setTargetConnectionFactories(
                Map.of("firstVendorConnectionFactory", firstVendorConnectionFactory,
                        "secondVendorConnectionFactory", secondVendorConnectionFactory));

        return simpleRoutingConnectionFactory;
    }

    @Bean
    public RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry() {
        return new RabbitListenerEndpointRegistry();
    }

    @Bean
    public RabbitListenerAnnotationBeanPostProcessor postProcessor(RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry) {
        final var postProcessor = new MultiRabbitListenerAnnotationBeanPostProcessor();
        postProcessor.setEndpointRegistry(rabbitListenerEndpointRegistry);
        postProcessor.setContainerFactoryBeanName("defaultContainerFactory");

        return postProcessor;
    }

    @Bean
    RabbitTemplate rabbitTemplate(SimpleRoutingConnectionFactory simpleRoutingConnectionFactory) {
        return new RabbitTemplate(simpleRoutingConnectionFactory);
    }

    @Bean
    ConnectionFactoryContextWrapper connectionFactoryContextWrapper(SimpleRoutingConnectionFactory simpleRoutingConnectionFactory) {
        return new ConnectionFactoryContextWrapper(simpleRoutingConnectionFactory);
    }
}
