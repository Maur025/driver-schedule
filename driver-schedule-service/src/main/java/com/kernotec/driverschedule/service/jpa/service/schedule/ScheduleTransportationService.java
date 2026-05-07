package com.kernotec.driverschedule.service.jpa.service.schedule;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.driverschedule.service.common.security.SecurityAuthProvider;
import com.kernotec.driverschedule.service.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverschedule.service.jpa.enums.resource.PersonTypeEnum;
import com.kernotec.driverschedule.service.jpa.enums.schedule.ScheduleTransportationStateEnum;
import com.kernotec.driverschedule.service.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverschedule.service.jpa.repository.schedule.ScheduleTransportationRepository;
import com.kernotec.driverschedule.service.jpa.service.resource.PersonService;
import com.kernotec.driverschedule.service.jpa.specification.schedule.ScheduleTransportationSpecification;
import com.kernotec.driverschedule.service.rest.dto.resource.request.AvailabilityForAssignmentRequest;
import com.kernotec.driverschedule.service.rest.dto.schedule.request.schedule.transportation.ScheduleTransportationFilterRequest;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ScheduleTransportationService extends BaseServiceImpl<ScheduleTransportation, UUID> {

    private final ScheduleTransportationRepository repository;
    private final SecurityAuthProvider securityAuthProvider;
    private final PersonService personService;

    @Override
    protected String resourceName() {
        return "Schedule Transportation";
    }

    @Override
    protected BaseRepository<ScheduleTransportation, UUID> repository() {
        return repository;
    }

    public Page<ScheduleTransportation> findConflictByVehicleIds(
        AvailabilityForAssignmentRequest request)
    {
        Pageable pageable = PageableUtil.of(0, 10, "createdAt", false);

        return repository.findAll(
            ScheduleTransportationSpecification.builder()
                .withConflictValidation(request.dateFrom(), request.dateTo())
                .withVehicleIds(request.vehicleIds())
                .withZoneId(request.zoneId())
                .withScheduleTransportationExcludeId(request.scheduleTransportationExcludeId())
                .withScheduleTransportationStates(List.of(
                    ScheduleTransportationStateEnum.SCHEDULED,
                    ScheduleTransportationStateEnum.RESCHEDULED,
                    ScheduleTransportationStateEnum.IN_PROGRESS
                ))
                .withTripStates(
                    Set.of(TripStateEnum.ON_ROUTE, TripStateEnum.WAITING, TripStateEnum.EMERGENCY)),
            pageable
        );
    }

    public Page<ScheduleTransportation> findConflictByDriverIds(
        AvailabilityForAssignmentRequest request)
    {
        Pageable pageable = PageableUtil.of(0, 10, "createdAt", false);

        return repository.findAll(
            ScheduleTransportationSpecification.builder()
                .withConflictValidation(request.dateFrom(), request.dateTo())
                .withDriverIds(request.driverIds())
                .withZoneId(request.zoneId())
                .withScheduleTransportationExcludeId(request.scheduleTransportationExcludeId())
                .withScheduleTransportationStates(Set.of(
                    ScheduleTransportationStateEnum.SCHEDULED,
                    ScheduleTransportationStateEnum.RESCHEDULED,
                    ScheduleTransportationStateEnum.IN_PROGRESS
                ))
                .withTripStates(
                    Set.of(TripStateEnum.ON_ROUTE, TripStateEnum.WAITING, TripStateEnum.EMERGENCY)),
            pageable
        );
    }

    public Page<ScheduleTransportation> findWhichVehicleBusy(UUID vehicleId) {
        return findWhichVehicleBusyByDate(vehicleId, null);
    }

    public Page<ScheduleTransportation> findWhichVehicleBusyByDate(UUID vehicleId,
        ZonedDateTime date)
    {
        Pageable pageable = PageableUtil.of(0, 10, "scheduleFrom", true);

        return repository.findAll(
            ScheduleTransportationSpecification.builder()
                .withVehicleId(vehicleId)
                .withGreaterThanOrEqualDate(date)
                .withScheduleTransportationStates(List.of(
                    ScheduleTransportationStateEnum.SCHEDULED,
                    ScheduleTransportationStateEnum.RESCHEDULED,
                    ScheduleTransportationStateEnum.IN_PROGRESS
                ))
                .withTripStates(
                    Set.of(TripStateEnum.ON_ROUTE, TripStateEnum.WAITING, TripStateEnum.EMERGENCY)),
            pageable
        );
    }

    public Page<ScheduleTransportation> findAllBySearch(
        ScheduleTransportationFilterRequest filterRequest, Pageable pageable)
    {
        var scheduleTransportationSpecification = ScheduleTransportationSpecification.builder()
            .withTransportationRequestId(filterRequest.getTransportationRequestId())
            .withDriverId(filterRequest.getDriverId())
            .withVehicleId(filterRequest.getVehicleId())
            .withScheduleTransportationState(filterRequest.getScheduleTransportationState())
            .withZoneId(filterRequest.getZoneId())
            .withSimpleDate(filterRequest.getSimpleDate())
            .withDateRange(filterRequest.getFromDate(), filterRequest.getToDate())
            .withMonthDate(filterRequest.getMonthDate())
            .withYearDate(filterRequest.getYearDate())
            .withScheduleTransportationStates(filterRequest.getScheduleTransportationStates())
            .withKeyword(filterRequest.getKeyword())
            .withPersonRequestedId(filterRequest.getPersonRequestedId());

        boolean hasOnlyOneRole = securityAuthProvider.hasOnlyOneRole();
        boolean isApplicant = securityAuthProvider.userContainsRole(PersonTypeEnum.APPLICANT);
        boolean isDriver = securityAuthProvider.userContainsRole(PersonTypeEnum.DRIVER);

        UUID personAuthenticateId = personService.findIdByUserIdAuthenticateThrow();

        if (!hasOnlyOneRole) {
            return repository.findAll(scheduleTransportationSpecification, pageable);
        }

        if (isApplicant) {
            scheduleTransportationSpecification.withPersonRequestedId(personAuthenticateId);
        }

        if (isDriver) {
            scheduleTransportationSpecification.withDriverId(personAuthenticateId);
        }

        return repository.findAll(scheduleTransportationSpecification, pageable);
    }
}
