package com.kernotec.driverschedule.service.scheduling.command.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripEmergencyLog;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TripEmergencyLogService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripEmergencyLogCreateCmd extends
    AbstractTransactionalRequiredCommand<TripEmergencyLogCreateCmd.Request, UUID>
{

    private final TripEmergencyLogService tripEmergencyLogService;

    @Override
    protected UUID run(Request request) {
        var tripEmergencyLog = new TripEmergencyLog();

        tripEmergencyLog.setTripEmergencyId(request.tripEmergencyId);
        tripEmergencyLog.setTripEmergencyStateId(request.tripEmergencyStateId);

        tripEmergencyLog = tripEmergencyLogService.save(tripEmergencyLog);
        return tripEmergencyLog.getId();
    }

    @Builder
    public record Request(@NotNull UUID tripEmergencyId, @NotNull UUID tripEmergencyStateId) {

    }
}
