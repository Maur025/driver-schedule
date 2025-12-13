package com.kernotec.driverscheduleservice.command.schedule.transportation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.service.ScheduleTransportationService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScheduleTransportationUpdateCmd extends
    AbstractTransactionalRequiredCommand<ScheduleTransportationUpdateCmd.Request, Void>
{

    private final ScheduleTransportationService scheduleTransportationService;

    @Override
    protected Void run(Request request) {
        ScheduleTransportation scheduleTransportation = scheduleTransportationService.findByIdThrow(
            request.scheduleTransportationId);

        if (request.scheduleFrom != null) {
            scheduleTransportation.setScheduleFrom(request.scheduleFrom);
        }
        if (request.scheduleTo != null) {
            scheduleTransportation.setScheduleTo(request.scheduleTo);
        }
        if (request.vehicleId != null) {
            scheduleTransportation.setVehicleId(request.vehicleId);
        }
        if (request.driverId != null) {
            scheduleTransportation.setDriverId(request.driverId);
        }
        if (request.scheduleTransportationStateId != null) {
            scheduleTransportation.setScheduleTransportationStateId(
                request.scheduleTransportationStateId);
        }

        scheduleTransportationService.save(scheduleTransportation);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId, ZonedDateTime scheduleFrom,
                          ZonedDateTime scheduleTo, UUID vehicleId, UUID driverId,
                          UUID scheduleTransportationStateId)
    {

    }
}
