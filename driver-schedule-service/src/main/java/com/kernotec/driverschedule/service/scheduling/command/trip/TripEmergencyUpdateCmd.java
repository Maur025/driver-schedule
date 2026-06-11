package com.kernotec.driverschedule.service.scheduling.command.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripEmergency;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TripEmergencyService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripEmergencyUpdateCmd extends
    AbstractTransactionalRequiredCommand<TripEmergencyUpdateCmd.Request, UUID>
{

    private final TripEmergencyService tripEmergencyService;

    @Override
    protected UUID run(Request request) {
        TripEmergency tripEmergency = tripEmergencyService.findByIdThrow(request.tripEmergencyId);

        if (request.personEmergencyReportedId != null) {
            tripEmergency.setPersonEmergencyReportedId(request.personEmergencyReportedId);
        }
        if (request.tripId != null) {
            tripEmergency.setTripId(request.tripId);
        }
        if (request.scheduleTransportationId != null) {
            tripEmergency.setScheduleTransportationId(request.scheduleTransportationId);
        }
        if (request.tripEmergencyStateId != null) {
            tripEmergency.setTripEmergencyStateId(request.tripEmergencyStateId);
        }

        tripEmergencyService.save(tripEmergency);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID tripEmergencyId, UUID personEmergencyReportedId,
                          UUID tripId, UUID scheduleTransportationId, UUID tripEmergencyStateId)
    {

    }
}
