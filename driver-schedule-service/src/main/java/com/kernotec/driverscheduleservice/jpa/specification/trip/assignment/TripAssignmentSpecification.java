package com.kernotec.driverscheduleservice.jpa.specification.trip.assignment;

import com.kernotec.driverscheduleservice.jpa.entity.TripAssignment;
import com.kernotec.driverscheduleservice.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.specification.criteria.TripAssignmentSpecificationCriteria;
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

public record TripAssignmentSpecification(TripAssignmentSpecificationCriteria criteria) implements
    Specification<TripAssignment>
{

    public static TripAssignmentSpecification builder() {
        return new TripAssignmentSpecification(new TripAssignmentSpecificationCriteria());
    }

    private Join<?, ?> getOrCreateScheduleTransportationJoin(
        Map<TripAssignmentSpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(
            TripAssignmentSpecificationJoinEnum.SCHEDULE_TRANSPORTATION_JOIN))
        {
            joinMap.put(
                TripAssignmentSpecificationJoinEnum.SCHEDULE_TRANSPORTATION_JOIN,
                root.join("scheduleTransportation", JoinType.INNER)
            );
        }

        return joinMap.get(TripAssignmentSpecificationJoinEnum.SCHEDULE_TRANSPORTATION_JOIN);
    }

    private Join<?, ?> getOrCreateScheduleTransportationStateJoin(
        Map<TripAssignmentSpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(
            TripAssignmentSpecificationJoinEnum.SCHEDULE_TRANSPORTATION_STATE_JOIN))
        {
            joinMap.put(
                TripAssignmentSpecificationJoinEnum.SCHEDULE_TRANSPORTATION_STATE_JOIN,
                getOrCreateScheduleTransportationJoin(joinMap, root).join(
                    "scheduleTransportationState", JoinType.INNER)
            );
        }

        return joinMap.get(TripAssignmentSpecificationJoinEnum.SCHEDULE_TRANSPORTATION_STATE_JOIN);
    }

    @Override
    public Predicate toPredicate(Root<TripAssignment> root, CriteriaQuery<?> query,
        CriteriaBuilder cb)
    {
        List<Predicate> predicateList = new ArrayList<>();
        Map<TripAssignmentSpecificationJoinEnum, Join<?, ?>> joinMap = new HashMap<>();

        addSimpleDateFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addDateRangeFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addMonthDateFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addYearDateFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addScheduleTransportationStatesFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addDriverIdFilter(root, cb).ifPresent(predicateList::add);
        addVehicleIdFilter(root, cb).ifPresent(predicateList::add);

        query.distinct(true);
        return cb.and(predicateList.toArray(Predicate[]::new));
    }

    public TripAssignmentSpecification withZoneId(String zoneId) {
        this.criteria.setZoneId(zoneId);
        return this;
    }

    public TripAssignmentSpecification withSimpleDate(ZonedDateTime simpleDate) {
        this.criteria.setSimpleDate(simpleDate);
        return this;
    }

    private Optional<Predicate> addSimpleDateFilter(Root<TripAssignment> root, CriteriaBuilder cb,
        Map<TripAssignmentSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getSimpleDate())
            .map(simpleDate -> CommonSpecification.simpleDatePredicate(
                cb, getOrCreateScheduleTransportationJoin(joinMap, root).get("scheduleFrom"),
                simpleDate, criteria.getZoneId()
            ));
    }

    public TripAssignmentSpecification withDateRange(ZonedDateTime fromDate, ZonedDateTime toDate) {
        this.criteria.setFromDate(fromDate);
        this.criteria.setToDate(toDate);
        return this;
    }

    private Optional<Predicate> addDateRangeFilter(Root<TripAssignment> root, CriteriaBuilder cb,
        Map<TripAssignmentSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        ZonedDateTime from = criteria.getFromDate();
        ZonedDateTime to = criteria.getToDate();

        if (from != null && to != null) {
            return Optional.of(CommonSpecification.dateRangePredicate(
                cb, getOrCreateScheduleTransportationJoin(joinMap, root).get("scheduleFrom"), from,
                to, criteria.getZoneId()
            ));
        }

        return Optional.empty();
    }

    public TripAssignmentSpecification withMonthDate(ZonedDateTime monthDate) {
        this.criteria.setMonthDate(monthDate);
        return this;
    }

    private Optional<Predicate> addMonthDateFilter(Root<TripAssignment> root, CriteriaBuilder cb,
        Map<TripAssignmentSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getMonthDate())
            .map(monthDate -> CommonSpecification.monthDatePredicate(
                cb, getOrCreateScheduleTransportationJoin(joinMap, root).get("scheduleFrom"),
                monthDate, criteria.getZoneId()
            ));
    }

    public TripAssignmentSpecification withYearDate(ZonedDateTime yearDate) {
        this.criteria.setYearDate(yearDate);
        return this;
    }

    private Optional<Predicate> addYearDateFilter(Root<TripAssignment> root, CriteriaBuilder cb,
        Map<TripAssignmentSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getYearDate())
            .map(yearDate -> CommonSpecification.yearDatePredicate(
                cb, getOrCreateScheduleTransportationJoin(joinMap, root).get("scheduleFrom"),
                yearDate, criteria.getZoneId()
            ));
    }

    public TripAssignmentSpecification withScheduleTransportationStates(
        Set<ScheduleTransportationStateEnum> scheduleTransportationStates)
    {
        this.criteria.setScheduleTransportationStates(scheduleTransportationStates);
        return this;
    }

    private Optional<Predicate> addScheduleTransportationStatesFilter(Root<TripAssignment> root,
        CriteriaBuilder cb, Map<TripAssignmentSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getScheduleTransportationStates())
            .map(scheduleTransportationStates -> getOrCreateScheduleTransportationStateJoin(
                joinMap, root).get("code")
                .in(scheduleTransportationStates.stream()
                    .map(String::valueOf)
                    .toList()));
    }

    public TripAssignmentSpecification withDriverId(UUID driverId) {
        this.criteria.setDriverId(driverId);
        return this;
    }

    private Optional<Predicate> addDriverIdFilter(Root<TripAssignment> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getDriverId())
            .map(driverId -> cb.equal(root.get("driverId"), driverId));
    }

    public TripAssignmentSpecification withVehicleId(UUID vehicleId) {
        this.criteria.setVehicleId(vehicleId);
        return this;
    }

    private Optional<Predicate> addVehicleIdFilter(Root<TripAssignment> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getVehicleId())
            .map(vehicleId -> cb.equal(root.get("vehicleId"), vehicleId));
    }
}
