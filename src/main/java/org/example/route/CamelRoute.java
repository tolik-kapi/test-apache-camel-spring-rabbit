package org.example.route;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CamelRoute extends RouteBuilder {

    private static final String SPRING_RABBIT_DEV_URI = "spring-rabbitmq:{{rife.insurance.dev-events.exchange-name}}"
            + "?queues={{rife.insurance.dev-events.queue-name}}"
            + "&routingKey={{rife.insurance.events.routing-key}}";

    private static final String SPRING_RABBIT_REC_URI = "spring-rabbitmq:{{rife.insurance.rec-events.exchange-name}}"
            + "?queues={{rife.insurance.rec-events.queue-name}}"
            + "&routingKey={{rife.insurance.events.routing-key}}";

    @Override
    public void configure() {

        from(SPRING_RABBIT_DEV_URI)
                .log("From DEV RabbitMQ: \n ROUTING KEY : ${headers.CamelSpringRabbitmqRoutingKey} \n BODY: ${body}");

        from(SPRING_RABBIT_REC_URI)
                .log("From REC RabbitMQ: \n ROUTING KEY : ${headers.CamelSpringRabbitmqRoutingKey} \n BODY: ${body}");
    }
}
