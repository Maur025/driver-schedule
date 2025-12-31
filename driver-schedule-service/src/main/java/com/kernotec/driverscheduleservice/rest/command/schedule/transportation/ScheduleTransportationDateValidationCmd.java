package com.kernotec.driverscheduleservice.rest.command.schedule.transportation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.exception.ScheduleTransportationException;
import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.service.ScheduleTransportationService;
import com.kernotec.driverscheduleservice.util.ZonedDateTimeUtil;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleTransportationDateValidationCmd extends
    AbstractTransactionalRequiredCommand<ScheduleTransportationDateValidationCmd.Request, Void>
{

    private final ZonedDateTimeUtil zonedDateTimeUtil;
    private final ScheduleTransportationService scheduleTransportationService;

    @Override
    protected Void run(Request request) {
        ZonedDateTime scheduledFrom = zonedDateTimeUtil.getNewOfDateAndTime(
            request.requestedDate,
            request.requestedStartTime
        );

        ZonedDateTime scheduledTo = zonedDateTimeUtil.getNewOfDateAndTime(
            request.requestedDate,
            request.requestedEndTime
        );

        if (scheduledTo.isBefore(scheduledFrom) || scheduledTo.isEqual(scheduledFrom)) {
            throw new ScheduleTransportationException(
                "to.less.or.equal.from", "", HttpStatus.BAD_REQUEST.value());
        }

        ZonedDateTime timeNow = ZonedDateTime.now();

        if (scheduledFrom.isBefore(timeNow)) {
            throw new ScheduleTransportationException(
                "invalid.range.date", "", HttpStatus.BAD_REQUEST.value());
        }

        List<ScheduleTransportation> vehicleConflictList = scheduleTransportationService.findConflictByVehicleId(
            request.vehicleId, scheduledFrom, scheduledTo, request.zoneId,
            request.scheduleTransportationExcludeId
        );

        if (!vehicleConflictList.isEmpty()) {
            throw new ScheduleTransportationException(
                "vehicle.conflict",
                "'" + request.vehicleId + "'", HttpStatus.CONFLICT.value()
            );
        }

        List<ScheduleTransportation> driverConflictList = scheduleTransportationService.findConflictByDriverId(
            request.driverId, scheduledFrom, scheduledTo, request.zoneId,
            request.scheduleTransportationExcludeId
        );

        if (!driverConflictList.isEmpty()) {
            throw new ScheduleTransportationException(
                "driver.conflict",
                "'" + request.driverId + "'", HttpStatus.CONFLICT.value()
            );
        }

        return null;
    }

    @Builder
    public record Request(@NotNull UUID vehicleId, @NotNull UUID driverId,
                          @NotNull ZonedDateTime requestedDate,
                          @NotNull ZonedDateTime requestedStartTime,
                          @NotNull ZonedDateTime requestedEndTime, String zoneId,
                          UUID scheduleTransportationExcludeId)
    {

    }
}
