package org.example.route;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Route extends RouteBuilder {

    @Override
    public void configure() {
        testConfigSplitter();
        testConfigMultiTenant();
    }


    private void testConfigSplitter() {
        final var SPRING_RABBIT_DEV_URI = "spring-rabbitmq:{{events.insurance.splitter.dev-events.exchange-name}}"
                + "?queues={{events.insurance.splitter.dev-events.queue-name}}"
                + "&routingKey={{events.insurance.splitter.events.routing-key}}";

        final var SPRING_RABBIT_TEST_URI = "spring-rabbitmq:{{events.insurance.splitter.test-events.exchange-name}}"
                + "?queues={{events.insurance.splitter.test-events.queue-name}}"
                + "&routingKey={{events.insurance.splitter.events.routing-key}}";

        from(SPRING_RABBIT_DEV_URI)
                .log("From DEV RabbitMQ: \n ROUTING KEY : ${headers.CamelSpringRabbitmqRoutingKey} \n BODY: ${body}");

        from(SPRING_RABBIT_TEST_URI)
                .log("From TEST RabbitMQ: \n ROUTING KEY : ${headers.CamelSpringRabbitmqRoutingKey} \n BODY: ${body}");
    }

    private void testConfigMultiTenant() {
        final var FIRST_VENDOR_ACTIVE_MQ = "firstVendorActiveMQ:queue:{{events.insurance.multitenant.first-vendor.queue-name}}";
        final var FIRST_VENDOR_RABBIT_MQ = "spring-rabbitmq::{{events.insurance.multitenant.first-vendor.exchange-name}}"
                + "?exchangeType=topic&connectionFactory=#firstVendorConnectionFactory";

        final var SECOND_VENDOR_ACTIVE_MQ = "secondVendorActiveMQ:queue:{{events.insurance.multitenant.second-vendor.queue-name}}";
        final var SECOND_VENDOR_RABBIT_MQ = "spring-rabbitmq:{{events.insurance.multitenant.second-vendor.exchange-name}}"
                + "?exchangeType=topic&connectionFactory=#secondVendorConnectionFactory";


        from(FIRST_VENDOR_ACTIVE_MQ)
                .routeId("first-vendor-active-mq-route")
                .log("!!!!!! FIRST VENDOR CUSTOM COMPONENT BEGIN !!!!!!")
                .log("${body}")
                .log("!!!!!!! FIRST VENDOR CUSTOM COMPONENT END !!!!!!!")
                .to(FIRST_VENDOR_RABBIT_MQ);

        from(SECOND_VENDOR_ACTIVE_MQ)
                .routeId("second-vendor-active-mq-route")
                .log("###### SECOND_VENDOR CUSTOM COMPONENT BEGIN ######")
                .log("${body}")
                .log("####### SECOND VENDOR CUSTOM COMPONENT END #######")
                .to(SECOND_VENDOR_RABBIT_MQ);
    }
}
