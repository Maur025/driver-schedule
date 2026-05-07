package com.kernotec.driverschedule.service.jpa.service.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.driverschedule.common.security.auth.SecurityAuthProvider;
import com.kernotec.driverschedule.service.exception.trip.TripException;
import com.kernotec.driverschedule.service.jpa.entity.trip.Trip;
import com.kernotec.driverschedule.person.jpa.enums.PersonTypeEnum;
import com.kernotec.driverschedule.service.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverschedule.service.jpa.repository.trip.TripRepository;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
import com.kernotec.driverschedule.service.jpa.specification.trip.TripSpecification;
import com.kernotec.driverschedule.service.rest.dto.trip.request.trip.TripFilterRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    public List<Trip> findByIdInAndDeleted(Set<UUID> tripIds, Boolean deleted) {
        return repository.findByIdInAndDeleted(tripIds, deleted);
    }

    public List<Trip> findByIdIn(Set<UUID> tripIds) {
        return findByIdInAndDeleted(tripIds, false);
    }

    public Page<Trip> findCurrentTrips() {
        boolean userHasRoleDriver = securityAuthProvider.userContainsRole(PersonTypeEnum.DRIVER);

        if (!userHasRoleDriver) {
            throw new TripException("", "", HttpStatus.CONFLICT.value());
        }

        Pageable pageable = PageableUtil.of(0, 3, "createdAt", false);
        UUID driverId = personService.findIdByUserIdAuthenticateThrow();

        return repository.findAll(
            TripSpecification.builder()
                .withTripStates(
                    Set.of(TripStateEnum.ON_ROUTE, TripStateEnum.WAITING, TripStateEnum.EMERGENCY))
                .withDriverId(driverId)
                .withDeleted(false), pageable
        );
    }

    public Trip findCurrentTrip() {
        Page<Trip> tripPage = findCurrentTrips();

        if (tripPage.isEmpty()) {
            return null;
        }

        return tripPage.getContent()
            .get(0);
    }
}
