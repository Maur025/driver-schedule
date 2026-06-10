package com.kernotec.driverschedule.service.scheduling.command.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.jpa.dto.TripDto;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.Trip;
import com.kernotec.driverschedule.service.scheduling.jpa.mapper.TripDtoMapper;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TripService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripGetDtoCmd extends
    AbstractTransactionalRequiredCommand<TripGetDtoCmd.Request, TripDto>
{

    private final TripService tripService;
    private final TripDtoMapper tripDtoMapper;

    @Override
    protected TripDto run(Request request) {
        Trip trip = tripService.findByIdThrow(request.tripId);
        return tripDtoMapper.toDto(trip);
    }

    @Builder
    public record Request(@NotNull UUID tripId) {

    }
}
