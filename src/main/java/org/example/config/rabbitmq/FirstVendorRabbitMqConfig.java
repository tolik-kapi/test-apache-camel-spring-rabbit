package org.example.config.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "broker.rabbitmq-first-vendor")
public class FirstVendorRabbitMqConfig {
    String host;
    Integer port;
    String username;
    String password;
    String virtualHost;

    @Bean
    CachingConnectionFactory firstVendorConnectionFactory() {
        final var connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);

        log.info("Creating Second Vendor connection factory with: %s @ %s : %s / %s".formatted(username, host, port, virtualHost));

        return connectionFactory;
    }

    @Bean
    RabbitAdmin hcbeAmqpAdmin() {
        return new RabbitAdmin(firstVendorConnectionFactory());
    }

    @Bean
    public SimpleRabbitListenerContainerFactory hcbeListenerContainerFactory(@Qualifier("firstVendorConnectionFactory") CachingConnectionFactory firstVendorConnectionFactory) {
        final var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(firstVendorConnectionFactory);

        return factory;
    }
}
