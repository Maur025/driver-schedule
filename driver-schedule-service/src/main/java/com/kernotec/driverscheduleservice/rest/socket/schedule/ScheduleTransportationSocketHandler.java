package com.kernotec.driverscheduleservice.rest.socket.schedule;

import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationService;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationResponseMapper;
import com.kernotec.driverscheduleservice.rest.socket.SocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class ScheduleTransportationSocketHandler extends
    SocketHandler<ScheduleTransportationSocketHandler.Request, ScheduleTransportationResponse>
{

    private final ScheduleTransportationService scheduleTransportationService;
    private final ScheduleTransportationResponseMapper scheduleTransportationResponseMapper;

    public ScheduleTransportationSocketHandler(WebSocketHandler webSocketHandler,
        ScheduleTransportationService service, ScheduleTransportationResponseMapper mapper)
    {
        super(webSocketHandler);
        this.scheduleTransportationService = service;
        this.scheduleTransportationResponseMapper = mapper;
    }

    @Override
    protected String getTopic(Request request) {
        return request.topic();
    }

    @Override
    protected Set<UUID> getToList(Request request) {
        return request.toList();
    }

    @Override
    protected ScheduleTransportationResponse getResponseData(Request request) {
        ScheduleTransportation scheduleTransportation = scheduleTransportationService.findByIdThrow(
            request.scheduleTransportationId());

        return scheduleTransportationResponseMapper.toResponse(scheduleTransportation);
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId, @NotNull String topic,
                          Set<UUID> toList)
    {

    }
}
