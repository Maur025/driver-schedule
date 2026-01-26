package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.enums.PersonTypeEnum;
import com.kernotec.driverscheduleservice.jpa.repository.ScheduleTransportationRepository;
import com.kernotec.driverscheduleservice.jpa.specification.schedule.transportation.ScheduleTransportationSpecification;
import com.kernotec.driverscheduleservice.rest.dto.request.schedule.transportation.ScheduleTransportationFilterRequest;
import com.kernotec.driverscheduleservice.util.AuthUtil;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ScheduleTransportationService extends BaseServiceImpl<ScheduleTransportation, UUID> {

    private final ScheduleTransportationRepository repository;
    private final AuthUtil authUtil;

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
        return repository.findAll(ScheduleTransportationSpecification.builder()
            .withConflictValidation(from, to)
            .withVehicleId(vehicleId)
            .withZoneId(zoneId)
            .withScheduleTransportationExcludeId(scheduleTransportationExcludeId));
    }

    public List<ScheduleTransportation> findConflictByVehicleId(UUID vehicleId, ZonedDateTime from,
        ZonedDateTime to, String zoneId)
    {
        return findConflictByVehicleId(vehicleId, from, to, zoneId, null);
    }

    public List<ScheduleTransportation> findConflictByDriverId(UUID driverId, ZonedDateTime from,
        ZonedDateTime to, String zoneId, UUID scheduleTransportationExcludeId)
    {
        return repository.findAll(ScheduleTransportationSpecification.builder()
            .withConflictValidation(from, to)
            .withDriverId(driverId)
            .withZoneId(zoneId)
            .withScheduleTransportationExcludeId(scheduleTransportationExcludeId));
    }

    public List<ScheduleTransportation> findConflictByDriverId(UUID driverId, ZonedDateTime from,
        ZonedDateTime to, String zoneId)
    {
        return findConflictByDriverId(driverId, from, to, zoneId, null);
    }

    public Page<ScheduleTransportation> findAllBySearch(
        ScheduleTransportationFilterRequest filterRequest, Authentication authentication,
        Pageable pageable)
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
            .withScheduleTransportationStates(filterRequest.getScheduleTransportationStates());

        boolean isAdmin = authUtil.userContainsRole(authentication, PersonTypeEnum.ADMIN);
        boolean isApplicant = authUtil.userContainsRole(authentication, PersonTypeEnum.APPLICANT);

        if (!isAdmin && isApplicant) {
            scheduleTransportationSpecification.withPersonRequestedId(
                authUtil.getPersonIdFromAuthenticationThrow(authentication));
        }

        return repository.findAll(scheduleTransportationSpecification, pageable);
    }
}
