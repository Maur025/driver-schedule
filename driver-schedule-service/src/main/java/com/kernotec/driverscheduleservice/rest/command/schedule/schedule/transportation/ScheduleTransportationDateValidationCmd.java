package com.kernotec.driverscheduleservice.rest.command.schedule.schedule.transportation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.exception.schedule.ScheduleTransportationException;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationService;
import com.kernotec.driverscheduleservice.util.ZonedDateTimeUtil;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
            request.requestedStartTime, request.zoneId
        );

        ZonedDateTime scheduledTo = zonedDateTimeUtil.getNewOfDateAndTime(
            request.requestedDate,
            request.requestedEndTime, request.zoneId
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

        List<ScheduleTransportation> vehicleConflictList = scheduleTransportationService.findConflictByVehicleIds(
            request.vehicleIdList, scheduledFrom, scheduledTo, request.zoneId,
            request.scheduleTransportationExcludeId
        );

        if (!vehicleConflictList.isEmpty()) {
            throw new ScheduleTransportationException(
                "vehicle.conflict", "",
                HttpStatus.CONFLICT.value()
            );
        }

        List<ScheduleTransportation> driverConflictList = scheduleTransportationService.findConflictByDriverIds(
            request.driverIdList, scheduledFrom, scheduledTo, request.zoneId,
            request.scheduleTransportationExcludeId
        );

        if (!driverConflictList.isEmpty()) {
            throw new ScheduleTransportationException(
                "driver.conflict", "",
                HttpStatus.CONFLICT.value()
            );
        }

        return null;
    }

    @Builder
    public record Request(@NotNull List<UUID> vehicleIdList, @NotNull List<UUID> driverIdList,
                          @NotNull LocalDateTime requestedDate,
                          @NotNull ZonedDateTime requestedStartTime,
                          @NotNull ZonedDateTime requestedEndTime, String zoneId,
                          UUID scheduleTransportationExcludeId)
    {

    }
}
