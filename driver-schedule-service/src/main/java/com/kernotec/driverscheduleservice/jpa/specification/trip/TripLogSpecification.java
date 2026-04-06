package com.kernotec.driverscheduleservice.jpa.specification.trip;

import com.kernotec.driverscheduleservice.jpa.entity.trip.TripLog;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverscheduleservice.jpa.specification.trip.criteria.TripLogSpecificationCriteria;
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

public record TripLogSpecification(TripLogSpecificationCriteria criteria) implements
    Specification<TripLog>
{

    public static TripLogSpecification builder() {
        return new TripLogSpecification(new TripLogSpecificationCriteria());
    }

    private Join<?, ?> getOrCreateTripStateJoin(
        Map<TripLogSpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(TripLogSpecificationJoinEnum.TRIP_STATES_JOIN)) {
            joinMap.put(
                TripLogSpecificationJoinEnum.TRIP_STATES_JOIN,
                root.join("tripState", JoinType.INNER)
            );
        }

        return joinMap.get(TripLogSpecificationJoinEnum.TRIP_STATES_JOIN);
    }

    @Override
    public Predicate toPredicate(Root<TripLog> root, CriteriaQuery<?> query, CriteriaBuilder cb)
    {
        List<Predicate> predicateList = new ArrayList<>();
        Map<TripLogSpecificationJoinEnum, Join<?, ?>> joinMap = new HashMap<>();

        addSimpleDateFilter(root, cb).ifPresent(predicateList::add);
        addDateRangeFilter(root, cb).ifPresent(predicateList::add);
        addMonthDateFilter(root, cb).ifPresent(predicateList::add);
        addYearDateFilter(root, cb).ifPresent(predicateList::add);

        addTripIdFilter(root, cb).ifPresent(predicateList::add);
        addTripStatesFilter(root, cb, joinMap).ifPresent(predicateList::add);

        query.distinct(true);
        return cb.and(predicateList.toArray(Predicate[]::new));
    }

    public TripLogSpecification withZoneId(String zoneId) {
        this.criteria.setZoneId(zoneId);
        return this;
    }

    public TripLogSpecification withSimpleDate(ZonedDateTime simpleDate) {
        this.criteria.setSimpleDate(simpleDate);
        return this;
    }

    private Optional<Predicate> addSimpleDateFilter(Root<TripLog> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getSimpleDate())
            .map(simpleDate -> CommonSpecification.simpleDatePredicate(
                cb, root.get("createdAt"),
                simpleDate, criteria.getZoneId()
            ));
    }

    public TripLogSpecification withDateRange(ZonedDateTime fromDate, ZonedDateTime toDate) {
        this.criteria.setFromDate(fromDate);
        this.criteria.setToDate(toDate);
        return this;
    }

    private Optional<Predicate> addDateRangeFilter(Root<TripLog> root, CriteriaBuilder cb) {
        ZonedDateTime from = criteria.getFromDate();
        ZonedDateTime to = criteria.getToDate();

        if (from != null && to != null) {
            return Optional.of(
                CommonSpecification.dateRangePredicate(
                    cb, root.get("createdAt"), from, to, criteria.getZoneId()));
        }

        return Optional.empty();
    }

    public TripLogSpecification withMonthDate(ZonedDateTime monthDate) {
        this.criteria.setMonthDate(monthDate);
        return this;
    }

    private Optional<Predicate> addMonthDateFilter(Root<TripLog> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getMonthDate())
            .map(monthDate -> CommonSpecification.monthDatePredicate(
                cb, root.get("createdAt"),
                monthDate, criteria.getZoneId()
            ));
    }

    public TripLogSpecification withYearDate(ZonedDateTime yearDate) {
        this.criteria.setYearDate(yearDate);
        return this;
    }

    private Optional<Predicate> addYearDateFilter(Root<TripLog> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getYearDate())
            .map(yearDate -> CommonSpecification.yearDatePredicate(
                cb, root.get("createdAt"),
                yearDate, criteria.getZoneId()
            ));
    }

    public TripLogSpecification withTripId(UUID tripId) {
        this.criteria.setTripId(tripId);
        return this;
    }

    private Optional<Predicate> addTripIdFilter(Root<TripLog> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getTripId())
            .map(tripId -> cb.equal(root.get("tripId"), tripId));
    }

    public TripLogSpecification withTripStates(Set<TripStateEnum> tripStates) {
        this.criteria.setTripStates(tripStates);
        return this;
    }

    private Optional<Predicate> addTripStatesFilter(Root<TripLog> root, CriteriaBuilder cb,
        Map<TripLogSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getTripStates())
            .map(tripStates -> getOrCreateTripStateJoin(joinMap, root).get("code")
                .in(tripStates.stream()
                    .map(String::valueOf)
                    .toList()));
    }
}
