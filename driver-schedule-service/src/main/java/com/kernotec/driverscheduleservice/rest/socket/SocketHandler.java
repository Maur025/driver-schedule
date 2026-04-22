package com.kernotec.driverscheduleservice.rest.socket;

import com.kernotec.driverscheduleservice.rest.dto.common.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

public abstract class SocketHandler<T, R> {

    private final WebSocketHandler webSocketHandler;

    protected SocketHandler(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    protected abstract String getTopic(T t);

    protected WebSocketSingleResponse.WebSocketSingleResponseBuilder<R> getBaseResponseBuilder(T t)
    {
        return WebSocketSingleResponse.<R>builder()
            .timestamp(ZonedDateTime.now())
            .topic(getTopic(t));
    }

    protected abstract R getResponseData(T t);

    protected abstract Set<UUID> getToList(T t);

    public void emitMessage(T t) {
        String topic = getTopic(t);
        R r = getResponseData(t);

        WebSocketSingleResponse<R> response = getBaseResponseBuilder(t).data(r)
            .build();

        if (getToList(t) != null && !getToList(t).isEmpty()) {
            getToList(t).forEach(
                userId -> webSocketHandler.emitMessageToUser(userId, topic, response));

            return;
        }

        webSocketHandler.emitMessage(topic, response);
    }
}
