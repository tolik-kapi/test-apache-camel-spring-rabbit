package org.example.route;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CamelRoute extends RouteBuilder {

    private static final String SPRING_RABBIT_URI = "spring-rabbitmq:{{events.numbers.exchange}}"
            + "?queues={{events.numbers.queue}}"
            + "&routingKey={{events.numbers.routing-key}}";

    @Override
    public void configure() {

        from("timer:hello?period=10000")
                .transform(simple("Random number ${random(100,999)}"))
                .log("${body}")
                .to(SPRING_RABBIT_URI);

        from(SPRING_RABBIT_URI)
                .log("From RabbitMQ: ${body}");
    }
}
