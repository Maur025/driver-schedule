package com.kernotec.driverscheduleservice.command.trip.trip.emergency;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.trip.TripEmergency;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripEmergencyService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripEmergencyCreateCmd extends
    AbstractTransactionalRequiredCommand<TripEmergencyCreateCmd.Request, UUID>
{

    private final TripEmergencyService tripEmergencyService;

    @Override
    protected UUID run(Request request) {
        var tripEmergency = new TripEmergency();

        tripEmergency.setPersonEmergencyReportedId(request.personEmergencyReportedId);
        tripEmergency.setTripId(request.tripId);
        tripEmergency.setScheduleTransportationId(request.scheduleTransportationId);
        tripEmergency.setTripEmergencyStateId(request.tripEmergencyStateId);

        tripEmergency = tripEmergencyService.save(tripEmergency);
        return tripEmergency.getId();
    }

    @Builder
    public record Request(@NotNull UUID personEmergencyReportedId, @NotNull UUID tripId,
                          @NotNull UUID scheduleTransportationId,
                          @NotNull UUID tripEmergencyStateId)
    {

    }
}
