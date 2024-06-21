package org.example.config.activemq;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import java.util.List;

@Slf4j
@EnableJms
@Configuration
@ConfigurationProperties(prefix = "broker.activemq-first-vendor")
public class FirstVendorActiveMqConfig {
    String brokerUrl;
    String user;
    String password;

    @Bean
    public JmsComponent firstVendorActiveMQ() {
        final var connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        connectionFactory.setTrustedPackages(List.of("org.example"));

        return JmsComponent.jmsComponentAutoAcknowledge(connectionFactory);
    }
}
