package com.kernotec.driverschedule.service.jpa.specification.schedule;

import com.kernotec.driverschedule.service.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverschedule.service.jpa.enums.schedule.ScheduleTransportationStateEnum;
import com.kernotec.driverschedule.service.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverschedule.service.jpa.specification.schedule.criteria.ScheduleTransportationSpecificationCriteria;
import com.kernotec.driverschedule.service.util.CommonSpecification;
import com.kernotec.driverschedule.service.util.ZonedDateTimeUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
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

    private Join<?, ?> getOrCreateTransportationRequestJoin(
        Map<ScheduleTransportationSpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(
            ScheduleTransportationSpecificationJoinEnum.TRANSPORTATION_REQUEST_JOIN))
        {
            joinMap.put(
                ScheduleTransportationSpecificationJoinEnum.TRANSPORTATION_REQUEST_JOIN,
                root.join("transportationRequest", JoinType.INNER)
            );
        }

        return joinMap.get(ScheduleTransportationSpecificationJoinEnum.TRANSPORTATION_REQUEST_JOIN);
    }

    private Join<?, ?> getOrCreateTripJoin(
        Map<ScheduleTransportationSpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(ScheduleTransportationSpecificationJoinEnum.TRIP_JOIN)) {
            joinMap.put(
                ScheduleTransportationSpecificationJoinEnum.TRIP_JOIN,
                getOrCreateTripAssignmentJoin(joinMap, root).join("trips", JoinType.LEFT)
            );
        }

        return joinMap.get(ScheduleTransportationSpecificationJoinEnum.TRIP_JOIN);
    }

    private Join<?, ?> getOrCreateTripStateJoin(
        Map<ScheduleTransportationSpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(ScheduleTransportationSpecificationJoinEnum.TRIP_STATE_JOIN)) {
            joinMap.put(
                ScheduleTransportationSpecificationJoinEnum.TRIP_STATE_JOIN,
                getOrCreateTripJoin(joinMap, root).join("tripState", JoinType.LEFT)
            );
        }

        return joinMap.get(ScheduleTransportationSpecificationJoinEnum.TRIP_STATE_JOIN);
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
        addPersonRequestedIdFilter(root, cb).ifPresent(predicateList::add);
        addScheduleTransportationStatesFilter(root, joinMap).ifPresent(predicateList::add);
        addVehicleIdsFilter(root, joinMap).ifPresent(predicateList::add);
        addDriverIdsFilter(root, joinMap).ifPresent(predicateList::add);
        addKeywordFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addGreaterThanOrEqualDateFilter(root, cb).ifPresent(predicateList::add);
        addTripStatesFilter(root, cb, joinMap).ifPresent(predicateList::add);

        CommonSpecification.addSimpleDateFilter(root, cb, criteria, () -> root.get("scheduleFrom"))
            .ifPresent(predicateList::add);

        CommonSpecification.addDateRangeFilter(root, cb, criteria, () -> root.get("scheduleFrom"))
            .ifPresent(predicateList::add);

        CommonSpecification.addMonthDateFilter(root, cb, criteria, () -> root.get("scheduleFrom"))
            .ifPresent(predicateList::add);

        CommonSpecification.addYearDateFilter(root, cb, criteria, () -> root.get("scheduleFrom"))
            .ifPresent(predicateList::add);

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
                    getOrCreateTripAssignmentJoin(joinMap, root).get("vehicleId"),
                    vehicleId
                ));
    }

    public ScheduleTransportationSpecification withVehicleIds(Collection<UUID> vehicleIds) {
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

    public ScheduleTransportationSpecification withDriverIds(Collection<UUID> driverIds) {
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

    public ScheduleTransportationSpecification withDateRange(ZonedDateTime fromDate,
        ZonedDateTime toDate)
    {
        this.criteria.setFromDate(fromDate);
        this.criteria.setToDate(toDate);
        return this;
    }

    public ScheduleTransportationSpecification withMonthDate(ZonedDateTime monthDate) {
        this.criteria.setMonthDate(monthDate);
        return this;
    }

    public ScheduleTransportationSpecification withYearDate(ZonedDateTime yearDate) {
        this.criteria.setYearDate(yearDate);
        return this;
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
        Collection<ScheduleTransportationStateEnum> scheduleTransportationStates)
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

    public ScheduleTransportationSpecification withKeyword(String keyword) {
        this.criteria.setKeyword(keyword);
        return this;
    }

    private Optional<Predicate> addKeywordFilter(Root<ScheduleTransportation> root,
        CriteriaBuilder cb, Map<ScheduleTransportationSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getKeyword())
            .map(keyword -> {
                String pattern = "%" + keyword.toLowerCase() + "%";

                return cb.or(
                    cb.like(
                        cb.lower(getOrCreateTransportationRequestJoin(joinMap, root).get("code")),
                        pattern
                    ), cb.like(
                        getOrCreateTransportationRequestJoin(joinMap, root).get("correlative")
                            .as(String.class), pattern
                    )
                );
            });
    }

    public ScheduleTransportationSpecification withGreaterThanOrEqualDate(ZonedDateTime date)
    {
        this.criteria.setGreaterThanOrEqualDate(date);
        return this;
    }

    private Optional<Predicate> addGreaterThanOrEqualDateFilter(Root<ScheduleTransportation> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getGreaterThanOrEqualDate())
            .map(date -> {
                ZonedDateTime dataWithZone = date.withZoneSameInstant(ZoneOffset.UTC);
                log.info("date with same zone instant: {}", dataWithZone);

                return cb.greaterThanOrEqualTo(root.get("scheduleFrom"), dataWithZone);
            });
    }

    public ScheduleTransportationSpecification withTripStates(Collection<TripStateEnum> tripStates)
    {
        this.criteria.setTripStates(tripStates);
        return this;
    }

    private Optional<Predicate> addTripStatesFilter(Root<ScheduleTransportation> root,
        CriteriaBuilder cb, Map<ScheduleTransportationSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getTripStates())
            .map(tripStates -> cb.or(
                cb.isNull(getOrCreateTripJoin(joinMap, root).get("id")), getOrCreateTripStateJoin(
                    joinMap, root).get("code")
                    .in(tripStates.stream()
                        .map(String::valueOf)
                        .toList())
            ));
    }
}
