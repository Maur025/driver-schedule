package com.kernotec.driverschedule.service.rest.dto.common.response.web.socket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class WebSocketSingleResponse<R> extends WebSocketBaseResponse {

    private R data;

    public WebSocketSingleResponse() {
    }

    @Builder
    public WebSocketSingleResponse(String topic, ZonedDateTime timestamp, R data) {
        super(topic, timestamp);
        this.data = data;
    }
}
