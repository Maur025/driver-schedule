package com.kernotec.driverscheduleservice.rest.dto.response.web.socket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class WebSocketListResponse<R> extends WebSocketBaseResponse {

    private List<R> data;

    public WebSocketListResponse() {
    }

    @Builder
    public WebSocketListResponse(String topic, ZonedDateTime timestamp, List<R> data) {
        super(topic, timestamp);
        this.data = data;
    }
}
