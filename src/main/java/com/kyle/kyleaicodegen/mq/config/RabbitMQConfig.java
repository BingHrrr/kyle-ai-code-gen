package com.kyle.kyleaicodegen.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SCREENSHOT_QUEUE = "screenshot.queue";
    public static final String SCREENSHOT_EXCHANGE = "screenshot.exchange";
    public static final String SCREENSHOT_ROUTING_KEY = "screenshot.key";
    public static final String SCREENSHOT_DLQ = "screenshot.dlq";
    public static final String SCREENSHOT_RETRY_QUEUE = "screenshot.retry.queue";

    @Bean
    public Queue screenshotQueue() {
        return QueueBuilder.durable(SCREENSHOT_QUEUE).build();
    }

    @Bean
    public DirectExchange screenshotExchange() {
        return new DirectExchange(SCREENSHOT_EXCHANGE);
    }

    @Bean
    public Binding screenshotBinding() {
        return BindingBuilder.bind(screenshotQueue())
                .to(screenshotExchange())
                .with(SCREENSHOT_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
