package me.jibajo.ride_matching_service.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

//    @Value("${ride-offer.exchange}")
//    private String RIDE_OFFER_EXCHANGE;
//
//    @Value("${ride-offer.queue}")
//    private String RIDE_OFFER_QUEUE;
//
//    @Value("${ride-offer.routing-key}")
//    private String RIDE_OFFER_ROUTING_KEY;
//
//    @Bean
//    public TopicExchange rideOffersExchange() {
//        return new TopicExchange(RIDE_OFFER_EXCHANGE);
//    }
//
//    @Bean
//    public Queue rideOfferQueue() {
//        return new Queue(RIDE_OFFER_QUEUE);
//    }
//
//    @Bean
//    public Binding rideOfferBinding() {
//        return BindingBuilder.bind(rideOfferQueue())
//                .to(rideOffersExchange())
//                .with(RIDE_OFFER_ROUTING_KEY);
//    }


    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
//            ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(messageConverter());
//        return factory;
//    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return  template;
    }
}
