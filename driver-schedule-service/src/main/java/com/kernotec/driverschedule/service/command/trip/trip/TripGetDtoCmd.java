package com.kernotec.driverschedule.service.command.trip.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.jpa.dto.trip.TripDto;
import com.kernotec.driverschedule.service.jpa.dto.mapper.trip.TripDtoMapper;
import com.kernotec.driverschedule.service.jpa.entity.trip.Trip;
import com.kernotec.driverschedule.service.jpa.service.trip.TripService;
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
