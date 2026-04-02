package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.common.security.SecurityAuthProvider;
import com.kernotec.driverscheduleservice.jpa.entity.TripAssignment;
import com.kernotec.driverscheduleservice.jpa.enums.PersonTypeEnum;
import com.kernotec.driverscheduleservice.jpa.repository.TripAssignmentRepository;
import com.kernotec.driverscheduleservice.jpa.specification.trip.assignment.TripAssignmentSpecification;
import com.kernotec.driverscheduleservice.rest.dto.request.trip.assignment.TripAssignmentFilterRequest;
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
        boolean isDriver = securityAuthProvider.userContainsRole(PersonTypeEnum.DRIVER);

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
}
