package com.kernotec.driverscheduleservice.command.schedule.schedule.transportation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.dto.schedule.ScheduleTransportationDto;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.schedule.ScheduleTransportationDtoMapper;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationService;
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

    @Override
    protected ScheduleTransportationDto run(Request request) {
        ScheduleTransportation scheduleTransportation = scheduleTransportationService.findByIdThrow(
            request.scheduleTransportationId);
        return scheduleTransportationDtoMapper.toDto(scheduleTransportation);
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId) {

    }
}
