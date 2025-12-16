package com.kernotec.driverscheduleservice.web.socket;

import com.kernotec.driverscheduleservice.rest.dto.response.WebSocketBaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WebSocketHandler {

    private final SimpMessagingTemplate template;

    public void emitMessage(String topic, WebSocketBaseResponse message) {
        template.convertAndSend(topic, message);
    }
}
