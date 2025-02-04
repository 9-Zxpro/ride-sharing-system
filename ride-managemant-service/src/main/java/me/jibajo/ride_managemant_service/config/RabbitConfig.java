package me.jibajo.ride_managemant_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String RIDE_REQUESTS_QUEUE = "ride-requests";
    public static final String DRIVER_MATCHED_QUEUE = "driver-matched";
    public static final String RIDE_EXCHANGE = "ride-exchange";

    @Bean
    public TopicExchange rideExchange() {
        return new TopicExchange(RIDE_EXCHANGE);
    }

    @Bean
    public Queue rideRequestsQueue() {
        return new Queue(RIDE_REQUESTS_QUEUE);
    }

    @Bean
    public Queue driverMatchedQueue() {
        return new Queue(DRIVER_MATCHED_QUEUE);
    }

    @Bean
    public Binding rideRequestsBinding() {
        return BindingBuilder.bind(rideRequestsQueue())
                .to(rideExchange())
                .with("ride.request");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}