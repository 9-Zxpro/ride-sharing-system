package me.jibajo.ride_management_service.config;

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

//    @Value("${ride-booking-request.exchange}")
//    private String RIDE_BOOKING_EXCHANGE;
//
//    @Value("${ride-booking-request.queue}")
//    private String RIDE_BOOK_REQUEST_QUEUE;
//
//    @Value("${ride-booking-request.routing-key}")
//    private String RIDE_BOOK_ROUTING_KEY;
//
//    @Value("${ride-offer.exchange}")
//    private String rideOfferExchange;
//
//    @Value("${ride-offer.queue}")
//    private String rideOfferQueue;
//
//    @Value("${ride-offer.routing-key}")
//    private String rideOfferRoutingKey;
//
//    @Value("${found-captain.exchange}")
//    private String rideMatchingCaptainExchange;
//
//    @Value("${found-captain.queue}")
//    private String rideMatchingCaptainQueue;
//
//    @Value("${found-captain.routing-key}")
//    private String rideMatchingCaptainRoutingKey;
//
//    @Bean
//    public TopicExchange rideBookingExchange() {
//        return new TopicExchange(RIDE_BOOKING_EXCHANGE);
//    }
//
//    @Bean
//    public Queue rideBookRequestQueue() {
//        return new Queue(RIDE_BOOK_REQUEST_QUEUE);
//    }
//
//    @Bean
//    public Binding rideBookingRequestsBinding() {
//        return BindingBuilder.bind(rideBookRequestQueue())
//                .to(rideBookingExchange())
//                .with(RIDE_BOOK_ROUTING_KEY);
//    }
//
//    @Bean
//    public TopicExchange rideOfferExchange() {
//        return new TopicExchange(rideOfferExchange);
//    }
//
//    @Bean
//    public Queue rideOfferQueue() {
//        return new Queue(rideOfferQueue);
//    }
//
//    @Bean
//    public Binding rideOfferBinding() {
//        return BindingBuilder.bind(rideOfferQueue())
//                .to(rideOfferExchange())
//                .with(rideOfferRoutingKey);
//    }
//
//    @Bean
//    public TopicExchange rideMatchingCaptainExchange() {
//        return new TopicExchange(rideMatchingCaptainExchange);
//    }
//
//    @Bean
//    public Queue rideMatchingCaptainQueue() {
//        return new Queue(rideMatchingCaptainQueue);
//    }
//
//    @Bean
//    public Binding rideMatchingCaptainBinding() {
//        return BindingBuilder.bind(rideMatchingCaptainQueue())
//                .to(rideMatchingCaptainExchange())
//                .with(rideMatchingCaptainRoutingKey);
//    }

//    @Bean
//    public Queue driverMatchedQueue() {
//        return new Queue(DRIVER_MATCHED_QUEUE);
//    }

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