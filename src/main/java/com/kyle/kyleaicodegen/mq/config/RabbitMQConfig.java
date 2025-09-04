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
    public static final String SCREENSHOT_RETRY_QUEUE = "screenshot.retry.queue";

//    @Bean
//    public Queue screenshotQueue() {
//        return QueueBuilder.durable(SCREENSHOT_QUEUE).build();
//    }

    @Bean
    public DirectExchange screenshotExchange() {
        return new DirectExchange(SCREENSHOT_EXCHANGE);
    }

    /** 正常队列，绑定死信交换机 */
    @Bean
    public Queue screenshotQueue() {
        Map<String, Object> args = new HashMap<>();
        // 当 screenshot.queue 中的消息被拒绝、过期或消费失败时，
        // 它会被重新投递到交换机 screenshot.exchange，并使用 routing key = screenshot.retry
        args.put("x-dead-letter-exchange", SCREENSHOT_EXCHANGE);
        args.put("x-dead-letter-routing-key", "screenshot.retry");
        return QueueBuilder.durable(SCREENSHOT_QUEUE).withArguments(args)
                .build();
//        return new Queue(SCREENSHOT_QUEUE, true, false, false, args);
    }

    /** 重试队列，带TTL，过期后回到正常队列 */
    @Bean
    public Queue retryQueue() {
        Map<String, Object> args = new HashMap<>();
        //当 screenshot.retry.queue 中的消息 TTL 到期后，它会被投递到 同一个交换机 screenshot.exchange，
        //但 routing key 变成 "screenshot"，于是回到 screenshot.queue。
        args.put("x-dead-letter-exchange", SCREENSHOT_EXCHANGE);
        args.put("x-dead-letter-routing-key", SCREENSHOT_ROUTING_KEY);
        args.put("x-message-ttl", 10000); // 10秒后再投递到正常队列
        return QueueBuilder.durable(SCREENSHOT_RETRY_QUEUE).withArguments(args).build();
//        return new Queue(SCREENSHOT_RETRY_QUEUE, true, false, false, args);
    }

    /** 死信队列（超过最大重试次数后进入） */
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(SCREENSHOT_DLQ).build();
//        return new Queue(SCREENSHOT_DLQ, true);
    }

//    @Bean
//    public Binding screenshotBinding() {
//        return BindingBuilder.bind(screenshotQueue())
//                .to(screenshotExchange())
//                .with(SCREENSHOT_ROUTING_KEY);
//    }
    // 绑定正常队列和交换机
    @Bean
    public Binding bindingNormal(Queue screenshotQueue, DirectExchange exchange) {
        return BindingBuilder.bind(screenshotQueue).to(exchange).with("screenshot");
    }
    // 绑定重试队列和交换机
    @Bean
    public Binding bindingRetry(Queue retryQueue, DirectExchange exchange) {
        return BindingBuilder.bind(retryQueue).to(exchange).with("screenshot.retry");
    }
    // 绑定死信队列和交换机
    @Bean
    public Binding bindingDlq(Queue deadLetterQueue, DirectExchange exchange) {
        return BindingBuilder.bind(deadLetterQueue).to(exchange).with("screenshot.dlq");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
