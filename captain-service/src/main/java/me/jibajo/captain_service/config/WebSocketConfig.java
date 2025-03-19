package me.jibajo.captain_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // The endpoint that the captain's app will connect to for WebSocket communication.
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:8082/")
                .withSockJS(); 
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory broker with destination prefix /topic.
        config.enableSimpleBroker("/topic");
        // All messages sent from client to server must have a destination that starts with /app.
        config.setApplicationDestinationPrefixes("/app");
    }
}
