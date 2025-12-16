package com.kernotec.driverscheduleservice.web.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketBroker implements WebSocketMessageBrokerConfigurer {

    private final WebSocketConfigProperties webSocketConfigProperties;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(webSocketConfigProperties.getBrokerPrefix());
        registry.setApplicationDestinationPrefixes(webSocketConfigProperties.getAppPrefix());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(webSocketConfigProperties.getEndpoint())
            .setAllowedOriginPatterns("*");
    }
}
