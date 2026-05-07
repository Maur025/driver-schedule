package com.kernotec.driverschedule.service.rest.dto.common.response.web.socket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public abstract class WebSocketBaseResponse implements Serializable {

    private String topic;
    private ZonedDateTime timestamp;

    public WebSocketBaseResponse() {
    }

    public WebSocketBaseResponse(String topic, ZonedDateTime timestamp) {
        this.topic = topic;
        this.timestamp = timestamp;
    }
}
