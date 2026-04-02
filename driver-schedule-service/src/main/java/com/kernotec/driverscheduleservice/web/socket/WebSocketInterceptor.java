package com.kernotec.driverscheduleservice.web.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
            message, StompHeaderAccessor.class);

        if (accessor == null) {
            log.error("No STOMP header accessor");
            throw new MessageDeliveryException("No STOMP header accessor");
        }

        if (!StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        String authHeader = accessor.getFirstNativeHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("No valid Authorization header found");
            throw new MessageDeliveryException("No valid Authorization header found");
        }

        String token = authHeader.substring(7);

        try {
            Jwt jwt = jwtDecoder.decode(token);

            var auth = new JwtAuthenticationToken(jwt);

            accessor.setUser(auth);

            log.info("WebSocket connection authenticated for user: {}", auth.getName());
        } catch (JwtValidationException ex) {
            log.warn("WebSocket rejected: {}", ex.getMessage());
            throw new MessageDeliveryException("TOKEN_EXPIRED");
        } catch (Exception ex) {
            log.error("Token validation failed: {}", ex.getMessage(), ex);
            throw new MessageDeliveryException("Token validation failed");
        }

        return message;
    }
}
