package com.kernotec.driverschedule.service.command.trip.trip.emergency;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.jpa.dto.mapper.trip.TripEmergencyDtoMapper;
import com.kernotec.driverschedule.service.jpa.dto.trip.TripEmergencyDto;
import com.kernotec.driverschedule.service.jpa.entity.trip.TripEmergency;
import com.kernotec.driverschedule.service.jpa.service.trip.TripEmergencyService;
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

    @Override
    protected TripEmergencyDto run(Request request) {
        TripEmergency tripEmergency = tripEmergencyService.findByIdThrow(request.tripEmergencyId);
        return tripEmergencyDtoMapper.toDto(tripEmergency);
    }

    @Builder
    public record Request(@NotNull UUID tripEmergencyId) {

    }
}