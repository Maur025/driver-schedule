package com.kernotec.driverschedule.service.rest.command.schedule.schedule.transportation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.exception.schedule.ScheduleTransportationException;
import com.kernotec.driverschedule.service.jpa.entity.resource.Person;
import com.kernotec.driverschedule.service.jpa.entity.resource.Vehicle;
import com.kernotec.driverschedule.service.jpa.service.resource.AvailabilityForAssignmentService;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
import com.kernotec.driverschedule.service.jpa.service.resource.VehicleService;
import com.kernotec.driverschedule.service.rest.dto.resource.request.AvailabilityForAssignmentRequest;
import com.kernotec.driverschedule.service.rest.dto.resource.response.AvailabilityForAssignmentResponse;
import com.kernotec.driverschedule.service.util.ZonedDateTimeUtil;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
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
    private final VehicleService vehicleService;
    private final PersonService personService;

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

        validateVehiclesUsability(request.vehicleIdList());
        validateVehiclesAvailability(request, scheduledFrom, scheduledTo);

        validatePersonUsability(request.driverIdList());
        validatePersonAvailability(request, scheduledFrom, scheduledTo);

        return null;
    }

    private void validateVehiclesUsability(Set<UUID> vehicleSet) {
        List<Vehicle> vehicleNotUsableList = vehicleService.findCanNotUsed(vehicleSet);

        if (!vehicleNotUsableList.isEmpty()) {
            throw new ScheduleTransportationException(
                "vehicle.conflic", "", HttpStatus.CONFLICT.value());
        }
    }

    private void validateVehiclesAvailability(Request request, ZonedDateTime scheduledFrom,
        ZonedDateTime scheduledTo)
    {
        AvailabilityForAssignmentResponse vehicleAvailabilityResponse = availabilityForAssignmentService.checkVehicleIsAvailable(
            AvailabilityForAssignmentRequest.builder()
                .vehicleIds(request.vehicleIdList())
                .dateFrom(scheduledFrom)
                .dateTo(scheduledTo)
                .zoneId(request.zoneId())
                .scheduleTransportationExcludeId(request.scheduleTransportationExcludeId())
                .build());

        if (vehicleAvailabilityResponse.isHasConflict()) {
            throw new ScheduleTransportationException(
                "vehicle.conflict", "", HttpStatus.CONFLICT.value());
        }
    }

    private void validatePersonUsability(Set<UUID> personSet) {
        List<Person> personNotUsableList = personService.canNotBeUsedAsDriver(personSet);

        if (!personNotUsableList.isEmpty()) {
            throw new ScheduleTransportationException(
                "driver.conflict", "", HttpStatus.CONFLICT.value());
        }
    }

    private void validatePersonAvailability(Request request, ZonedDateTime scheduledFrom,
        ZonedDateTime scheduledTo)
    {
        AvailabilityForAssignmentResponse driverAvailabilityResponse = availabilityForAssignmentService.checkDriverIsAvailable(
            AvailabilityForAssignmentRequest.builder()
                .driverIds(request.driverIdList())
                .dateFrom(scheduledFrom)
                .dateTo(scheduledTo)
                .zoneId(request.zoneId())
                .scheduleTransportationExcludeId(request.scheduleTransportationExcludeId())
                .build());

        if (driverAvailabilityResponse.isHasConflict()) {
            throw new ScheduleTransportationException(
                "driver.conflict", "", HttpStatus.CONFLICT.value());
        }
    }

    @Builder
    public record Request(@NotNull Set<UUID> vehicleIdList, @NotNull Set<UUID> driverIdList,
                          @NotNull ZonedDateTime requestedStartTime,
                          @NotNull ZonedDateTime requestedEndTime, String zoneId,
                          UUID scheduleTransportationExcludeId)
    {

    }
}
