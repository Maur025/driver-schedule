package com.kernotec.driverschedule.service.rest.socket.schedule;

import com.kernotec.driverschedule.service.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverschedule.service.jpa.service.schedule.ScheduleTransportationService;
import com.kernotec.driverschedule.service.rest.dto.schedule.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverschedule.service.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationResponseMapper;
import com.kernotec.driverschedule.service.rest.socket.SocketHandler;
import com.kernotec.driverschedule.service.web.socket.WebSocketHandler;
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
