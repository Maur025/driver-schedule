package com.kernotec.driverschedule.service.scheduling.command.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.ScheduleTransportation;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripEmergency;
import com.kernotec.driverschedule.service.scheduling.jpa.mapper.ScheduleToTripEmergencyDtoMapper;
import com.kernotec.driverschedule.service.scheduling.jpa.service.ScheduleTransportationService;
import com.kernotec.driverschedule.service.scheduling.jpa.dto.TripEmergencyDto;
import com.kernotec.driverschedule.service.scheduling.jpa.mapper.TripEmergencyDtoMapper;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TripEmergencyService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripEmergencyGetDtoCmd extends
    AbstractTransactionalRequiredCommand<TripEmergencyGetDtoCmd.Request, TripEmergencyDto>
{

    private final TripEmergencyService tripEmergencyService;
    private final TripEmergencyDtoMapper tripEmergencyDtoMapper;
    private final ScheduleTransportationService scheduleTransportationService;
    private final ScheduleToTripEmergencyDtoMapper scheduleToTripEmergencyDtoMapper;

    @Override
    protected TripEmergencyDto run(Request request) {
        TripEmergency tripEmergency = tripEmergencyService.findByIdThrow(request.tripEmergencyId);
        TripEmergencyDto tripEmergencyDto = tripEmergencyDtoMapper.toDto(tripEmergency);

        if (tripEmergencyDto.getScheduleTransportation() == null) {
            ScheduleTransportation scheduleTransportation = scheduleTransportationService.findByIdThrow(
                tripEmergencyDto.getScheduleTransportationId());

            tripEmergencyDto.setScheduleTransportation(
                scheduleToTripEmergencyDtoMapper.toDto(scheduleTransportation));
        }

        return tripEmergencyDto;
    }

    @Builder
    public record Request(@NotNull UUID tripEmergencyId) {

    }
}