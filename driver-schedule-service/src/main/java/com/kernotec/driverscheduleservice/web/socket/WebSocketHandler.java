package com.kernotec.driverscheduleservice.web.socket;

import com.kernotec.driverscheduleservice.rest.dto.response.web.socket.WebSocketBaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebSocketHandler {

    private final SimpMessagingTemplate template;

    public void emitMessage(String topic, WebSocketBaseResponse message) {
        try {
            template.convertAndSend(topic, message);
        } catch (Exception ex) {
            log.error(
                "Error emitting websocket message to topic {}: {}", topic, ex.getMessage(), ex);
        }
    }
}
