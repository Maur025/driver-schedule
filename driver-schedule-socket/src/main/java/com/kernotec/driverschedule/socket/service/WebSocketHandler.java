package com.kernotec.driverschedule.socket.service;

import com.kernotec.driverschedule.common.response.WebSocketBaseResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebSocketHandler {

    private final SimpMessagingTemplate template;

    public void emitMessage(String topic, WebSocketBaseResponse message) {
        try {
            log.info("SEND MESSAGE TO TOPIC: [{}]", topic);
            template.convertAndSend(topic, message);
        } catch (MessagingException ex) {
            log.error(
                "Error emitting websocket message to topic [{}]: {}", topic, ex.getMessage(), ex);
        }
    }

    public void emitMessageToUser(UUID userId, String topic, WebSocketBaseResponse message) {
        emitMessageToUser(String.valueOf(userId), topic, message);
    }

    public void emitMessageToUser(String userId, String topic, WebSocketBaseResponse message) {
        try {
            template.convertAndSendToUser(String.valueOf(userId), topic, message);
        } catch (MessagingException ex) {
            log.error(
                "Error emitting websocket message to user {} on topic [{}]: {}", userId,
                topic, ex.getMessage(), ex
            );
        }
    }
}
