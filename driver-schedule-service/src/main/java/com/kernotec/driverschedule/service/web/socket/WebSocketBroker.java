package com.kernotec.driverschedule.service.web.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketBroker implements WebSocketMessageBrokerConfigurer {

    private final WebSocketConfigProperties webSocketConfigProperties;
    private final WebSocketInterceptor webSocketInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(
            webSocketConfigProperties.getBrokerPrefix(),
            webSocketConfigProperties.getPrivateMessagePrefix()
        );
        registry.setApplicationDestinationPrefixes(webSocketConfigProperties.getAppPrefix());
        registry.setUserDestinationPrefix(webSocketConfigProperties.getUserDestinationPrefix());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(webSocketConfigProperties.getEndpoint())
            .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketInterceptor);
    }
}
