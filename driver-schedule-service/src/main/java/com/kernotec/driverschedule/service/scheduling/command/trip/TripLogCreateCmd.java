package com.kernotec.driverschedule.service.scheduling.command.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripLog;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TripLogService;
import com.kernotec.driverschedule.common.dto.Coordinate;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripLogCreateCmd extends
    AbstractTransactionalRequiredCommand<TripLogCreateCmd.Request, UUID>
{

    private final TripLogService tripLogService;

    @Override
    protected UUID run(Request request) {
        var tripLog = new TripLog();

        tripLog.setTripId(request.tripId);
        tripLog.setTripStateId(request.tripStateId);
        tripLog.setCoordinate(request.coordinate);

        tripLog = tripLogService.save(tripLog);
        return tripLog.getId();
    }

    @Builder
    public record Request(@NotNull UUID tripId, @NotNull UUID tripStateId, Coordinate coordinate) {

    }
}
