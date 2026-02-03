package com.kernotec.driverscheduleservice.jpa.specification.schedule.transportation;

import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.specification.criteria.ScheduleTransportationSpecificationCriteria;
import com.kernotec.driverscheduleservice.util.CommonSpecification;
import com.kernotec.driverscheduleservice.util.ZonedDateTimeUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public record ScheduleTransportationSpecification(
    ScheduleTransportationSpecificationCriteria criteria) implements
    Specification<ScheduleTransportation>
{

    public static ScheduleTransportationSpecification builder() {
        return new ScheduleTransportationSpecification(
            new ScheduleTransportationSpecificationCriteria());
    }

    private Join<?, ?> getOrCreateScheduleTransportationStateJoin(
        Map<ScheduleTransportationSpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(
            ScheduleTransportationSpecificationJoinEnum.SCHEDULE_TRANSPORTATION_STATE_JOIN))
        {
            joinMap.put(
                ScheduleTransportationSpecificationJoinEnum.SCHEDULE_TRANSPORTATION_STATE_JOIN,
                root.join("scheduleTransportationState", JoinType.INNER)
            );
        }

        return joinMap.get(
            ScheduleTransportationSpecificationJoinEnum.SCHEDULE_TRANSPORTATION_STATE_JOIN);
    }

    private Join<?, ?> getOrCreateTripAssignmentJoin(
        Map<ScheduleTransportationSpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(
            ScheduleTransportationSpecificationJoinEnum.TRIP_ASSIGNMENT_JOIN))
        {
            joinMap.put(
                ScheduleTransportationSpecificationJoinEnum.TRIP_ASSIGNMENT_JOIN,
                root.join("tripAssignments", JoinType.INNER)
            );
        }

        return joinMap.get(ScheduleTransportationSpecificationJoinEnum.TRIP_ASSIGNMENT_JOIN);
    }

    @Override
    public Predicate toPredicate(Root<ScheduleTransportation> root, CriteriaQuery<?> query,
        CriteriaBuilder cb)
    {
        List<Predicate> predicateList = new ArrayList<>();
        Map<ScheduleTransportationSpecificationJoinEnum, Join<?, ?>> joinMap = new HashMap<>();

        addConflictValidationFilter(root, cb).ifPresent(predicateList::add);
        addVehicleIdFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addDriverIdFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addTransportationRequestIdFilter(root, cb).ifPresent(predicateList::add);
        addScheduleTransportationExcludeIdFilter(root, cb).ifPresent(predicateList::add);
        addScheduleTransportationStateFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addSimpleDateFilter(root, cb).ifPresent(predicateList::add);
        addDateRangeFilter(root, cb).ifPresent(predicateList::add);
        addMonthDateFilter(root, cb).ifPresent(predicateList::add);
        addYearDateFilter(root, cb).ifPresent(predicateList::add);
        addPersonRequestedIdFilter(root, cb).ifPresent(predicateList::add);
        addScheduleTransportationStatesFilter(root, joinMap).ifPresent(predicateList::add);
        addVehicleIdsFilter(root, joinMap).ifPresent(predicateList::add);
        addDriverIdsFilter(root, joinMap).ifPresent(predicateList::add);

        query.distinct(true);
        return cb.and(predicateList.toArray(Predicate[]::new));
    }

    public ScheduleTransportationSpecification withConflictValidation(
        ZonedDateTime conflictValidationFrom, ZonedDateTime conflictValidationTo)
    {
        this.criteria.setConflictValidationFrom(conflictValidationFrom);
        this.criteria.setConflictValidationTo(conflictValidationTo);
        return this;
    }

    private Optional<Predicate> addConflictValidationFilter(Root<ScheduleTransportation> root,
        CriteriaBuilder cb)
    {
        ZonedDateTime from = criteria.getConflictValidationFrom();
        ZonedDateTime to = criteria.getConflictValidationTo();

        if (from == null || to == null) {
            return Optional.empty();
        }

        ZoneId clientZoneId = ZonedDateTimeUtil.getClientZoneId(criteria.getZoneId());

        ZonedDateTime fromWithClientZone = from.withZoneSameInstant(clientZoneId);
        ZonedDateTime toWithClientZone = to.withZoneSameInstant(clientZoneId);

        return Optional.of(
            cb.and(
                cb.lessThan(root.get("scheduleFrom"), toWithClientZone),
                cb.greaterThan(root.get("scheduleTo"), fromWithClientZone)
            ));
    }

    public ScheduleTransportationSpecification withVehicleId(UUID vehicleId) {
        this.criteria.setVehicleId(vehicleId);
        return this;
    }

    private Optional<Predicate> addVehicleIdFilter(Root<ScheduleTransportation> root,
        CriteriaBuilder cb, Map<ScheduleTransportationSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getVehicleId())
            .map(
                vehicleId -> cb.equal(
                    getOrCreateTripAssignmentJoin(joinMap, root).get("vehicleId"), vehicleId));
    }

    public ScheduleTransportationSpecification withVehicleIds(List<UUID> vehicleIds) {
        this.criteria.setVehicleIds(vehicleIds);
        return this;
    }

    private Optional<Predicate> addVehicleIdsFilter(Root<ScheduleTransportation> root,
        Map<ScheduleTransportationSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getVehicleIds())
            .map(vehicleIds -> getOrCreateTripAssignmentJoin(joinMap, root).get("vehicleId")
                .in(vehicleIds));
    }

    public ScheduleTransportationSpecification withDriverId(UUID driverId) {
        this.criteria.setDriverId(driverId);
        return this;
    }

    private Optional<Predicate> addDriverIdFilter(Root<ScheduleTransportation> root,
        CriteriaBuilder cb, Map<ScheduleTransportationSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getDriverId())
            .map(driverId -> cb.equal(
                getOrCreateTripAssignmentJoin(joinMap, root).get("driverId"),
                driverId
            ));
    }

    public ScheduleTransportationSpecification withDriverIds(List<UUID> driverIds) {
        this.criteria.setDriverIds(driverIds);
        return this;
    }

    private Optional<Predicate> addDriverIdsFilter(Root<ScheduleTransportation> root,
        Map<ScheduleTransportationSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getDriverIds())
            .map(driverIds -> getOrCreateTripAssignmentJoin(joinMap, root).get("driverId")
                .in(driverIds));
    }

    public ScheduleTransportationSpecification withTransportationRequestId(
        UUID transportationRequestId)
    {
        this.criteria.setTransportationRequestId(transportationRequestId);
        return this;
    }

    private Optional<Predicate> addTransportationRequestIdFilter(Root<ScheduleTransportation> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getTransportationRequestId())
            .map(transportationRequestId -> cb.equal(
                root.get("transportationRequestId"),
                transportationRequestId
            ));
    }

    public ScheduleTransportationSpecification withZoneId(String zoneId) {
        this.criteria.setZoneId(zoneId);
        return this;
    }

    public ScheduleTransportationSpecification withScheduleTransportationExcludeId(
        UUID scheduleTransportationExcludeId)
    {
        this.criteria.setScheduleTransportationExcludeId(scheduleTransportationExcludeId);
        return this;
    }

    private Optional<Predicate> addScheduleTransportationExcludeIdFilter(
        Root<ScheduleTransportation> root, CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getScheduleTransportationExcludeId())
            .map(scheduleTransportationExcludeId -> cb.notEqual(
                root.get("id"),
                scheduleTransportationExcludeId
            ));
    }

    public ScheduleTransportationSpecification withScheduleTransportationState(
        ScheduleTransportationStateEnum scheduleTransportationState)
    {
        this.criteria.setScheduleTransportationState(scheduleTransportationState);
        return this;
    }

    private Optional<Predicate> addScheduleTransportationStateFilter(
        Root<ScheduleTransportation> root, CriteriaBuilder cb,
        Map<ScheduleTransportationSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getScheduleTransportationState())
            .map(
                scheduleTransportationState -> cb.equal(
                    getOrCreateScheduleTransportationStateJoin(joinMap, root).get("code"),
                    String.valueOf(scheduleTransportationState)
                ));
    }

    public ScheduleTransportationSpecification withSimpleDate(ZonedDateTime simpleDate) {
        this.criteria.setSimpleDate(simpleDate);
        return this;
    }

    private Optional<Predicate> addSimpleDateFilter(Root<ScheduleTransportation> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getSimpleDate())
            .map(simpleDate -> CommonSpecification.simpleDatePredicate(
                cb, root.get("createdAt"),
                simpleDate, criteria.getZoneId()
            ));
    }

    public ScheduleTransportationSpecification withDateRange(ZonedDateTime fromDate,
        ZonedDateTime toDate)
    {
        this.criteria.setFromDate(fromDate);
        this.criteria.setToDate(toDate);
        return this;
    }

    private Optional<Predicate> addDateRangeFilter(Root<ScheduleTransportation> root,
        CriteriaBuilder cb)
    {
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

    public ScheduleTransportationSpecification withMonthDate(ZonedDateTime monthDate) {
        this.criteria.setMonthDate(monthDate);
        return this;
    }

    private Optional<Predicate> addMonthDateFilter(Root<ScheduleTransportation> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getMonthDate())
            .map(monthDate -> CommonSpecification.monthDatePredicate(
                cb, root.get("createdAt"),
                monthDate, criteria.getZoneId()
            ));
    }

    public ScheduleTransportationSpecification withYearDate(ZonedDateTime yearDate) {
        this.criteria.setYearDate(yearDate);
        return this;
    }

    private Optional<Predicate> addYearDateFilter(Root<ScheduleTransportation> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getYearDate())
            .map(yearDate -> CommonSpecification.yearDatePredicate(
                cb, root.get("createdAt"),
                yearDate, criteria.getZoneId()
            ));
    }

    public ScheduleTransportationSpecification withPersonRequestedId(UUID personRequestedId) {
        this.criteria.setPersonRequestedId(personRequestedId);
        return this;
    }

    private Optional<Predicate> addPersonRequestedIdFilter(Root<ScheduleTransportation> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getPersonRequestedId())
            .map(personRequestedId -> cb.equal(root.get("personRequestedId"), personRequestedId));
    }

    public ScheduleTransportationSpecification withScheduleTransportationStates(
        List<ScheduleTransportationStateEnum> scheduleTransportationStates)
    {
        this.criteria.setScheduleTransportationStates(scheduleTransportationStates);
        return this;
    }

    private Optional<Predicate> addScheduleTransportationStatesFilter(
        Root<ScheduleTransportation> root,
        Map<ScheduleTransportationSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getScheduleTransportationStates())
            .map(scheduleTransportationStates -> getOrCreateScheduleTransportationStateJoin(
                joinMap, root).get("code")
                .in(scheduleTransportationStates.stream()
                    .map(String::valueOf)
                    .toList()));
    }
}
