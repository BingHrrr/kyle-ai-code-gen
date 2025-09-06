package com.kyle.kyleaicodegen.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String SCREENSHOT_QUEUE = "screenshot.queue";
    public static final String SCREENSHOT_EXCHANGE = "screenshot.exchange";
    public static final String SCREENSHOT_ROUTING_KEY = "screenshot";

    public static final String SCREENSHOT_DLQ = "screenshot.dlq";
    public static final String SCREENSHOT_DLQ_EXCHANGE = "screenshot.dlx";
    public static final String SCREENSHOT_DLQ_ROUTING_KEY = "screenshot.dlq";

    @Bean
    public DirectExchange screenshotExchange() {
        return new DirectExchange(SCREENSHOT_EXCHANGE);
    }

    @Bean
    public DirectExchange screenshotDlxExchange() {
        return new DirectExchange(SCREENSHOT_DLQ_EXCHANGE);
    }

    /** 正常队列，配置 DLX */
    @Bean
    public Queue screenshotQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", SCREENSHOT_DLQ_EXCHANGE);
        args.put("x-dead-letter-routing-key", SCREENSHOT_DLQ_ROUTING_KEY);
        return QueueBuilder.durable(SCREENSHOT_QUEUE).withArguments(args).build();
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(SCREENSHOT_DLQ).build();
    }

    /**
     * 正常队列绑定正常交换机
     */
    @Bean
    public Binding bindingNormal(Queue screenshotQueue, DirectExchange screenshotExchange) {
        return BindingBuilder.bind(screenshotQueue).to(screenshotExchange).with(SCREENSHOT_ROUTING_KEY);
    }

    /** 死信队列绑定死信交换机 */
    @Bean
    public Binding bindingDlq(Queue deadLetterQueue, DirectExchange screenshotDlxExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(screenshotDlxExchange).with(SCREENSHOT_DLQ_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}