package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.driverscheduleservice.common.security.SecurityAuthProvider;
import com.kernotec.driverscheduleservice.jpa.entity.Trip;
import com.kernotec.driverscheduleservice.jpa.enums.PersonTypeEnum;
import com.kernotec.driverscheduleservice.jpa.repository.TripRepository;
import com.kernotec.driverscheduleservice.jpa.specification.trip.TripSpecification;
import com.kernotec.driverscheduleservice.rest.dto.request.trip.TripFilterRequest;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TripService extends BaseServiceImpl<Trip, UUID> {

    private final TripRepository repository;
    private final SecurityAuthProvider securityAuthProvider;
    private final PersonService personService;

    @Override
    protected String resourceName() {
        return "Trip";
    }

    @Override
    protected BaseRepository<Trip, UUID> repository() {
        return repository;
    }

    public Optional<Trip> findByTripAssignmentIdAndDeleted(UUID tripAssignmentId, Boolean deleted) {
        return repository.findByTripAssignmentIdAndDeleted(tripAssignmentId, deleted);
    }

    public Optional<Trip> findByTripAssignmentId(UUID tripAssignmentId) {
        return findByTripAssignmentIdAndDeleted(tripAssignmentId, false);
    }

    public Page<Trip> findAllBySearch(TripFilterRequest filterRequest, Pageable pageable) {
        UUID driverId = filterRequest.getDriverId();

        boolean hasOnlyOneRole = securityAuthProvider.hasOnlyOneRole();
        boolean isDriver = securityAuthProvider.userContainsRole(PersonTypeEnum.DRIVER);

        if (hasOnlyOneRole && isDriver) {
            driverId = personService.findIdByUserIdAuthenticateThrow();
        }

        return repository.findAll(
            TripSpecification.builder()
                .withZoneId(filterRequest.getZoneId())
                .withSimpleDate(filterRequest.getSimpleDate())
                .withDateRange(filterRequest.getFromDate(), filterRequest.getToDate())
                .withMonthDate(filterRequest.getMonthDate())
                .withYearDate(filterRequest.getYearDate())
                .withDriverId(driverId)
                .withVehicleId(filterRequest.getVehicleId())
                .withTripStates(filterRequest.getTripStates())
                .withDeleted(filterRequest.getDeleted()), pageable
        );
    }

    public Page<Trip> findByTripAssignmentIdInAndDeleted(Set<UUID> tripAssignmentIds,
        Boolean deleted)
    {
        Pageable pageable = PageableUtil.of(
            0, Math.max(tripAssignmentIds.size(), 1), "createdAt", false);

        return repository.findByTripAssignmentIdInAndDeleted(tripAssignmentIds, deleted, pageable);
    }
}
