package me.jibajo.notification_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${ride-booking-request.exchange}")
    private String RIDE_BOOKING_EXCHANGE;

    @Value("${ride-booking-request.queue}")
    private String RIDE_BOOK_REQUEST_QUEUE;

    @Value("${ride-booking-request.routing-key}")
    private String RIDE_BOOK_ROUTING_KEY;


    @Bean
    public TopicExchange rideExchange() {
        return new TopicExchange(RIDE_BOOKING_EXCHANGE);
    }

    @Bean
    public Queue rideBookRequestQueue() {
        return new Queue(RIDE_BOOK_REQUEST_QUEUE);
    }

//    @Bean
//    public Queue driverMatchedQueue() {
//        return new Queue(DRIVER_MATCHED_QUEUE);
//    }

    @Bean
    public Binding rideRequestsBinding() {
        return BindingBuilder.bind(rideBookRequestQueue())
                .to(rideExchange())
                .with(RIDE_BOOK_ROUTING_KEY);
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