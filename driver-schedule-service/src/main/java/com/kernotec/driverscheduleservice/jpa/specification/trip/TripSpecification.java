package com.kernotec.driverscheduleservice.jpa.specification.trip;

import com.kernotec.driverscheduleservice.jpa.entity.trip.Trip;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverscheduleservice.jpa.specification.trip.criteria.TripSpecificationCriteria;
import com.kernotec.driverscheduleservice.util.CommonSpecification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public record TripSpecification(TripSpecificationCriteria criteria) implements Specification<Trip> {

    public static TripSpecification builder() {
        return new TripSpecification(new TripSpecificationCriteria());
    }

    private Join<?, ?> getOrCreateTripAssignmentJoin(
        Map<TripSpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(TripSpecificationJoinEnum.TRIP_ASSIGNMENT_JOIN)) {
            joinMap.put(
                TripSpecificationJoinEnum.TRIP_ASSIGNMENT_JOIN,
                root.join("tripAssignment", JoinType.INNER)
            );
        }

        return joinMap.get(TripSpecificationJoinEnum.TRIP_ASSIGNMENT_JOIN);
    }

    private Join<?, ?> getOrCreateTripStateJoin(Map<TripSpecificationJoinEnum, Join<?, ?>> joinMap,
        Root<?> root)
    {
        if (!joinMap.containsKey(TripSpecificationJoinEnum.TRIP_STATE_JOIN)) {
            joinMap.put(
                TripSpecificationJoinEnum.TRIP_STATE_JOIN, root.join("tripState", JoinType.INNER));
        }

        return joinMap.get(TripSpecificationJoinEnum.TRIP_STATE_JOIN);
    }

    @Override
    public Predicate toPredicate(Root<Trip> root, CriteriaQuery<?> query, CriteriaBuilder cb)
    {
        List<Predicate> predicateList = new ArrayList<>();
        Map<TripSpecificationJoinEnum, Join<?, ?>> joinMap = new HashMap<>();

        addSimpleDateFilter(root, cb).ifPresent(predicateList::add);
        addDateRangeFilter(root, cb).ifPresent(predicateList::add);
        addMonthDateFilter(root, cb).ifPresent(predicateList::add);
        addYearDateFilter(root, cb).ifPresent(predicateList::add);
        addDriverIdFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addVehicleIdFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addTripStatesFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addDeletedFilter(root, cb).ifPresent(predicateList::add);

        query.distinct(true);
        return cb.and(predicateList.toArray(Predicate[]::new));
    }

    public TripSpecification withZoneId(String zoneId) {
        this.criteria.setZoneId(zoneId);
        return this;
    }

    public TripSpecification withSimpleDate(ZonedDateTime simpleDate) {
        this.criteria.setSimpleDate(simpleDate);
        return this;
    }

    private Optional<Predicate> addSimpleDateFilter(Root<Trip> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getSimpleDate())
            .map(simpleDate -> CommonSpecification.simpleDatePredicate(
                cb, root.get("createdAt"),
                simpleDate, criteria.getZoneId()
            ));
    }

    public TripSpecification withDateRange(ZonedDateTime fromDate, ZonedDateTime toDate) {
        this.criteria.setFromDate(fromDate);
        this.criteria.setToDate(toDate);
        return this;
    }

    private Optional<Predicate> addDateRangeFilter(Root<Trip> root, CriteriaBuilder cb) {
        ZonedDateTime from = criteria.getFromDate();
        ZonedDateTime to = criteria.getToDate();

        if (from != null && to != null) {
            return Optional.of(
                CommonSpecification.dateRangePredicate(
                    cb, root.get("createdAt"), from, to,
                    criteria.getZoneId()
                ));
        }
        return Optional.empty();
    }

    public TripSpecification withMonthDate(ZonedDateTime monthDate) {
        this.criteria.setMonthDate(monthDate);
        return this;
    }

    private Optional<Predicate> addMonthDateFilter(Root<Trip> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getMonthDate())
            .map(monthDate -> CommonSpecification.monthDatePredicate(
                cb, root.get("createdAt"),
                monthDate, criteria.getZoneId()
            ));
    }

    public TripSpecification withYearDate(ZonedDateTime yearDate) {
        this.criteria.setYearDate(yearDate);
        return this;
    }

    private Optional<Predicate> addYearDateFilter(Root<Trip> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getYearDate())
            .map(yearDate -> CommonSpecification.yearDatePredicate(
                cb, root.get("createdAt"),
                yearDate, criteria.getZoneId()
            ));
    }

    public TripSpecification withDriverId(UUID driverId) {
        this.criteria.setDriverId(driverId);
        return this;
    }

    private Optional<Predicate> addDriverIdFilter(Root<Trip> root, CriteriaBuilder cb,
        Map<TripSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getDriverId())
            .map(driverId -> cb.equal(
                getOrCreateTripAssignmentJoin(joinMap, root).get("driverId"),
                driverId
            ));
    }

    public TripSpecification withVehicleId(UUID vehicleId) {
        this.criteria.setVehicleId(vehicleId);
        return this;
    }

    private Optional<Predicate> addVehicleIdFilter(Root<Trip> root, CriteriaBuilder cb,
        Map<TripSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getVehicleId())
            .map(
                vehicleId -> cb.equal(
                    getOrCreateTripAssignmentJoin(joinMap, root).get("vehicleId"),
                    vehicleId
                ));
    }

    public TripSpecification withTripStates(Set<TripStateEnum> tripStates) {
        this.criteria.setTripStates(tripStates);
        return this;
    }

    private Optional<Predicate> addTripStatesFilter(Root<Trip> root, CriteriaBuilder cb,
        Map<TripSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getTripStates())
            .map(tripStates -> getOrCreateTripStateJoin(joinMap, root).get("code")
                .in(tripStates.stream()
                    .map(String::valueOf)
                    .toList()));
    }

    public TripSpecification withDeleted(Boolean deleted) {
        this.criteria.setDeleted(deleted);
        return this;
    }

    private Optional<Predicate> addDeletedFilter(Root<Trip> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getDeleted())
            .map(deleted -> cb.equal(root.get("deleted"), deleted));
    }
}
