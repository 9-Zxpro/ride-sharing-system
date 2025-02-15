package me.jibajo.captain_service.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RabbitConfig {

    @Value("${ride-offer.exchange}")
    private String RIDE_OFFER_EXCHANGE;

    @Value("${ride-offer.queues-prefix}")
    private String RIDE_OFFER_QUEUE_PREFIX;

    @Value("${ride-offer.routing-key-prefix}")
    private String RIDE_OFFER_ROUTING_KEY_PREFIX;

    @Bean
    public TopicExchange rideOffersExchange() {
        return new TopicExchange(RIDE_OFFER_EXCHANGE);
    }

//    @Bean
//    public List<Queue> captainGroupQueues() {
//        List<Queue> queues = new ArrayList<>();
//        for (int i = 0; i < QUEUE_COUNT; i++) {
//            queues.add(new Queue(RIDE_OFFER_QUEUE_PREFIX + i));
//        }
//        return queues;
//    }

//    @Bean
//    public List<Queue> captainGroupQueues() {
//        List<Queue> queues = new ArrayList<>();
//        for (int i = 0; i < QUEUE_COUNT; i++) {
//            Queue queue = new Queue(RIDE_OFFER_QUEUE_PREFIX + i);
//            // Declare binding directly on the queue bean
//            BindingBuilder.bind(queue).to(rideOffersExchange()).with(RIDE_OFFER_ROUTING_KEY_PREFIX + i);
//            queues.add(queue);
//        }
//        return queues;
//    }


    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return  template;
    }
}
