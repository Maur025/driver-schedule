package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.driverscheduleservice.common.security.SecurityAuthProvider;
import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.enums.PersonTypeEnum;
import com.kernotec.driverscheduleservice.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.repository.ScheduleTransportationRepository;
import com.kernotec.driverscheduleservice.jpa.specification.schedule.transportation.ScheduleTransportationSpecification;
import com.kernotec.driverscheduleservice.rest.dto.request.schedule.transportation.ScheduleTransportationFilterRequest;
import java.time.ZonedDateTime;
import java.util.List;
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

    public List<ScheduleTransportation> findConflictByVehicleId(UUID vehicleId, ZonedDateTime from,
        ZonedDateTime to, String zoneId, UUID scheduleTransportationExcludeId)
    {
        // TODO: add pagination to prevent overflow
        return repository.findAll(ScheduleTransportationSpecification.builder()
            .withConflictValidation(from, to)
            .withVehicleId(vehicleId)
            .withZoneId(zoneId)
            .withScheduleTransportationExcludeId(scheduleTransportationExcludeId));
    }

    public List<ScheduleTransportation> findConflictByVehicleIds(List<UUID> vehicleIds,
        ZonedDateTime from, ZonedDateTime to, String zoneId, UUID scheduleTransportationExcludeId)
    {
        // TODO: add pagination to prevent overflow

        return repository.findAll(ScheduleTransportationSpecification.builder()
            .withConflictValidation(from, to)
            .withVehicleIds(vehicleIds)
            .withZoneId(zoneId)
            .withScheduleTransportationExcludeId(scheduleTransportationExcludeId)
            .withScheduleTransportationStates(List.of(
                ScheduleTransportationStateEnum.SCHEDULED,
                ScheduleTransportationStateEnum.RESCHEDULED,
                ScheduleTransportationStateEnum.IN_PROGRESS
            )));
    }

    public List<ScheduleTransportation> findConflictByDriverId(UUID driverId, ZonedDateTime from,
        ZonedDateTime to, String zoneId, UUID scheduleTransportationExcludeId)
    {
        // TODO: add pagination to prevent overflow
        return repository.findAll(ScheduleTransportationSpecification.builder()
            .withConflictValidation(from, to)
            .withDriverId(driverId)
            .withZoneId(zoneId)
            .withScheduleTransportationExcludeId(scheduleTransportationExcludeId));
    }

    public List<ScheduleTransportation> findConflictByDriverIds(List<UUID> driverIds,
        ZonedDateTime from, ZonedDateTime to, String zoneId, UUID scheduleTransportationExcludeId)
    {
        // TODO: add pagination to prevent overflow
        return repository.findAll(ScheduleTransportationSpecification.builder()
            .withConflictValidation(from, to)
            .withDriverIds(driverIds)
            .withZoneId(zoneId)
            .withScheduleTransportationExcludeId(scheduleTransportationExcludeId)
            .withScheduleTransportationStates(List.of(
                ScheduleTransportationStateEnum.SCHEDULED,
                ScheduleTransportationStateEnum.RESCHEDULED,
                ScheduleTransportationStateEnum.IN_PROGRESS
            )));
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
                )), pageable
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
