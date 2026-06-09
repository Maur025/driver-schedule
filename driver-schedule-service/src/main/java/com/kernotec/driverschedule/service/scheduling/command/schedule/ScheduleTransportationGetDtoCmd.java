package com.kernotec.driverschedule.service.scheduling.command.schedule;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TransportationRequest;
import com.kernotec.driverschedule.service.scheduling.jpa.mapper.TransportationRequestDtoFlatMapper;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TransportationRequestService;
import com.kernotec.driverschedule.service.scheduling.jpa.dto.ScheduleTransportationDto;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.ScheduleTransportation;
import com.kernotec.driverschedule.service.scheduling.jpa.mapper.ScheduleTransportationDtoMapper;
import com.kernotec.driverschedule.service.scheduling.jpa.service.ScheduleTransportationService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScheduleTransportationGetDtoCmd extends
    AbstractTransactionalRequiredCommand<ScheduleTransportationGetDtoCmd.Request, ScheduleTransportationDto>
{

    private final ScheduleTransportationService scheduleTransportationService;
    private final ScheduleTransportationDtoMapper scheduleTransportationDtoMapper;
    private final TransportationRequestService transportationRequestService;
    private final TransportationRequestDtoFlatMapper transportationRequestDtoFlatMapper;

    @Override
    protected ScheduleTransportationDto run(Request request) {
        ScheduleTransportation scheduleTransportation = scheduleTransportationService.findByIdThrow(
            request.scheduleTransportationId);

        ScheduleTransportationDto scheduleDto = scheduleTransportationDtoMapper.toDto(
            scheduleTransportation);

        if (scheduleDto.getTransportationRequest() == null) {
            TransportationRequest transportationRequest = transportationRequestService.findByIdThrow(
                scheduleDto.getTransportationRequestId());

            scheduleDto.setTransportationRequest(
                transportationRequestDtoFlatMapper.toDto(transportationRequest));
        }

        return scheduleDto;
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId) {

    }
}
