package com.kernotec.driverscheduleservice.rest.command.schedule.schedule.transportation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.exception.schedule.ScheduleTransportationException;
import com.kernotec.driverscheduleservice.jpa.service.resource.AvailabilityForAssignmentService;
import com.kernotec.driverscheduleservice.rest.dto.resource.request.AvailabilityForAssignmentRequest;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.AvailabilityForAssignmentResponse;
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
    private final AvailabilityForAssignmentService availabilityForAssignmentService;

    @Override
    protected Void run(Request request) {
        ZonedDateTime scheduledFrom = zonedDateTimeUtil.getDateScheduleNormalized(
            request.requestedStartTime);

        ZonedDateTime scheduledTo = zonedDateTimeUtil.getDateScheduleNormalized(
            request.requestedEndTime);

        if (scheduledTo.isBefore(scheduledFrom) || scheduledTo.isEqual(scheduledFrom)) {
            throw new ScheduleTransportationException(
                "to.less.or.equal.from", "", HttpStatus.BAD_REQUEST.value());
        }

        ZonedDateTime timeNow = ZonedDateTime.now()
            .minusMinutes(15)
            .withSecond(0)
            .withNano(0);

        if (scheduledFrom.isBefore(timeNow)) {
            throw new ScheduleTransportationException(
                "invalid.range.date", "", HttpStatus.BAD_REQUEST.value());
        }

        AvailabilityForAssignmentResponse vehicleAvailabilityResponse = availabilityForAssignmentService.checkVehicleIsAvailable(
            AvailabilityForAssignmentRequest.builder()
                .vehicleIds(request.vehicleIdList)
                .dateFrom(scheduledFrom)
                .dateTo(scheduledTo)
                .zoneId(request.zoneId)
                .scheduleTransportationExcludeId(request.scheduleTransportationExcludeId)
                .build());

        if (vehicleAvailabilityResponse.isHasConflict()) {
            throw new ScheduleTransportationException(
                "vehicle.conflict", "",
                HttpStatus.CONFLICT.value()
            );
        }

        AvailabilityForAssignmentResponse driverAvailabilityResponse = availabilityForAssignmentService.checkDriverIsAvailable(
            AvailabilityForAssignmentRequest.builder()
                .driverIds(request.driverIdList)
                .dateFrom(scheduledFrom)
                .dateTo(scheduledTo)
                .zoneId(request.zoneId)
                .scheduleTransportationExcludeId(request.scheduleTransportationExcludeId)
                .build());

        if (driverAvailabilityResponse.isHasConflict()) {
            throw new ScheduleTransportationException(
                "driver.conflict", "",
                HttpStatus.CONFLICT.value()
            );
        }

        return null;
    }

    @Builder
    public record Request(@NotNull List<UUID> vehicleIdList, @NotNull List<UUID> driverIdList,
                          @NotNull ZonedDateTime requestedStartTime,
                          @NotNull ZonedDateTime requestedEndTime, String zoneId,
                          UUID scheduleTransportationExcludeId)
    {

    }
}
