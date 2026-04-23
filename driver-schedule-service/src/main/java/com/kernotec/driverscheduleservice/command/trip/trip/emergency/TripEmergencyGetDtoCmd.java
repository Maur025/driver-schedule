package com.kernotec.driverscheduleservice.command.trip.trip.emergency;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.trip.TripEmergencyDtoMapper;
import com.kernotec.driverscheduleservice.jpa.dto.trip.TripEmergencyDto;
import com.kernotec.driverscheduleservice.jpa.entity.trip.TripEmergency;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripEmergencyService;
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