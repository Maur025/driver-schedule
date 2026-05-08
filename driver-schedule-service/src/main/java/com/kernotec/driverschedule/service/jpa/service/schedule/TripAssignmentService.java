package com.kernotec.driverschedule.service.jpa.service.schedule;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.driverschedule.common.security.auth.SecurityAuthProvider;
import com.kernotec.driverschedule.common.security.auth.UserRoleType;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
import com.kernotec.driverschedule.service.jpa.entity.schedule.TripAssignment;
import com.kernotec.driverschedule.service.jpa.enums.schedule.ScheduleTransportationStateEnum;
import com.kernotec.driverschedule.service.jpa.enums.schedule.TripAssignmentStateCodeEnum;
import com.kernotec.driverschedule.service.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverschedule.service.jpa.repository.schedule.TripAssignmentRepository;
import com.kernotec.driverschedule.service.jpa.specification.schedule.TripAssignmentSpecification;
import com.kernotec.driverschedule.service.rest.dto.resource.request.AvailabilityForAssignmentRequest;
import com.kernotec.driverschedule.service.rest.dto.schedule.request.trip.assignment.TripAssignmentFilterRequest;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class TripAssignmentService extends BaseServiceImpl<TripAssignment, UUID> {

    private final TripAssignmentRepository repository;
    private final SecurityAuthProvider securityAuthProvider;
    private final PersonService personService;

    @Override
    protected String resourceName() {
        return "Trip Assignment";
    }

    @Override
    protected BaseRepository<TripAssignment, UUID> repository() {
        return repository;
    }

    @Transactional
    public void deleteAllByScheduleTransportationId(UUID scheduleTransportationId) {
        repository.deleteAllByScheduleTransportationId(scheduleTransportationId);
    }

    public Page<TripAssignment> findAllBySearch(TripAssignmentFilterRequest filterRequest,
        Pageable pageable)
    {
        UUID driverId = filterRequest.getDriverId();

        boolean hasOnlyOneRole = securityAuthProvider.hasOnlyOneRole();
        boolean isDriver = securityAuthProvider.userContainsRole(UserRoleType.DRIVER);

        if (hasOnlyOneRole && isDriver) {
            driverId = personService.findIdByUserIdAuthenticateThrow();
        }

        return repository.findAll(
            TripAssignmentSpecification.builder()
                .withZoneId(filterRequest.getZoneId())
                .withSimpleDate(filterRequest.getSimpleDate())
                .withDateRange(filterRequest.getFromDate(), filterRequest.getToDate())
                .withMonthDate(filterRequest.getMonthDate())
                .withYearDate(filterRequest.getYearDate())
                .withScheduleTransportationStates(filterRequest.getScheduleTransportationStates())
                .withDriverId(driverId)
                .withVehicleId(filterRequest.getVehicleId())
                .withTripStates(filterRequest.getTripStates()), pageable
        );
    }

    public Page<TripAssignment> findConflictByVehicleIds(AvailabilityForAssignmentRequest request) {
        Pageable pageable = PageableUtil.of(0, 10, "createdAt", false);

        return repository.findAll(
            TripAssignmentSpecification.builder()
                .withAvailabilityValidation(request.dateFrom(), request.dateTo())
                .withVehicleIds(request.vehicleIds())
                .withZoneId(request.zoneId())
                .withScheduleTransportationExcludeId(request.scheduleTransportationExcludeId())
                .withScheduleTransportationStates(Set.of(
                    ScheduleTransportationStateEnum.SCHEDULED,
                    ScheduleTransportationStateEnum.RESCHEDULED,
                    ScheduleTransportationStateEnum.IN_PROGRESS
                ))
                .withExistingTripStates(
                    Set.of(TripStateEnum.ON_ROUTE, TripStateEnum.WAITING, TripStateEnum.EMERGENCY))
                .withTripAssignmentStates(Set.of(TripAssignmentStateCodeEnum.ACTIVE)), pageable
        );
    }

    public Page<TripAssignment> findConflictByDriverIds(AvailabilityForAssignmentRequest request) {
        Pageable pageable = PageableUtil.of(0, 10, "createdAt", false);

        return repository.findAll(
            TripAssignmentSpecification.builder()
                .withAvailabilityValidation(request.dateFrom(), request.dateTo())
                .withDriverIds(request.driverIds())
                .withZoneId(request.zoneId())
                .withScheduleTransportationExcludeId(request.scheduleTransportationExcludeId())
                .withScheduleTransportationStates(Set.of(
                    ScheduleTransportationStateEnum.SCHEDULED,
                    ScheduleTransportationStateEnum.RESCHEDULED,
                    ScheduleTransportationStateEnum.IN_PROGRESS
                ))
                .withExistingTripStates(
                    Set.of(TripStateEnum.ON_ROUTE, TripStateEnum.WAITING, TripStateEnum.EMERGENCY))
                .withTripAssignmentStates(Set.of(TripAssignmentStateCodeEnum.ACTIVE)), pageable
        );
    }

    public Page<TripAssignment> findConflictsToDisableVehicle(UUID vehicleId) {
        return findConflictsToDisableVehicle(vehicleId, null);
    }

    public Page<TripAssignment> findConflictsToDisableVehicle(UUID vehicleId,
        ZonedDateTime dateTime)
    {
        Pageable pageable = PageableUtil.of(0, 10, "scheduleFrom", true);

        return repository.findAll(
            TripAssignmentSpecification.builder()
                .withVehicleId(vehicleId)
                .withGreaterThanOrEqualDate(dateTime)
                .withScheduleTransportationStates(Set.of(
                    ScheduleTransportationStateEnum.SCHEDULED,
                    ScheduleTransportationStateEnum.RESCHEDULED,
                    ScheduleTransportationStateEnum.IN_PROGRESS
                ))
                .withExistingTripStates(
                    Set.of(TripStateEnum.ON_ROUTE, TripStateEnum.WAITING, TripStateEnum.EMERGENCY))
                .withTripAssignmentStates(Set.of(TripAssignmentStateCodeEnum.ACTIVE)), pageable
        );
    }
}
