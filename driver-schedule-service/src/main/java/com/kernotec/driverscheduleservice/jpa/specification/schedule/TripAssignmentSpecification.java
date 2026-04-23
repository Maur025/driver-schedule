package com.kernotec.driverscheduleservice.jpa.specification.schedule;

import com.kernotec.driverscheduleservice.jpa.entity.schedule.TripAssignment;
import com.kernotec.driverscheduleservice.jpa.enums.schedule.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.schedule.TripAssignmentStateCodeEnum;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverscheduleservice.jpa.specification.schedule.criteria.TripAssignmentSpecificationCriteria;
import com.kernotec.driverscheduleservice.util.CommonSpecification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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

    private Join<?, ?> getOrCreateTripJoin(
        Map<TripAssignmentSpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(TripAssignmentSpecificationJoinEnum.TRIP_JOIN)) {
            joinMap.put(
                TripAssignmentSpecificationJoinEnum.TRIP_JOIN, root.join("trips", JoinType.LEFT));
        }

        return joinMap.get(TripAssignmentSpecificationJoinEnum.TRIP_JOIN);
    }

    private Join<?, ?> getOrCreateTripStateJoin(
        Map<TripAssignmentSpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(TripAssignmentSpecificationJoinEnum.TRIP_STATE_JOIN)) {
            joinMap.put(
                TripAssignmentSpecificationJoinEnum.TRIP_STATE_JOIN,
                getOrCreateTripJoin(joinMap, root).join("tripState", JoinType.LEFT)
            );
        }

        return joinMap.get(TripAssignmentSpecificationJoinEnum.TRIP_STATE_JOIN);
    }

    private Join<?, ?> getOrCreateTripAssignmentStateJoin(
        Map<TripAssignmentSpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(TripAssignmentSpecificationJoinEnum.TRIP_ASSIGNMENT_STATE)) {
            joinMap.put(
                TripAssignmentSpecificationJoinEnum.TRIP_ASSIGNMENT_STATE,
                root.join("tripAssignmentState", JoinType.INNER)
            );
        }

        return joinMap.get(TripAssignmentSpecificationJoinEnum.TRIP_ASSIGNMENT_STATE);
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
        addTripStatesFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addVehicleIdsFilter(root, cb).ifPresent(predicateList::add);
        addDriverIdsFilter(root, cb).ifPresent(predicateList::add);
        addGreaterThanOrEqualDateFilter(root, cb).ifPresent(predicateList::add);
        addAvailabilityValidationFilter(root, cb).ifPresent(predicateList::add);
        addExistingTripStatesFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addTripAssignmentStatesFilter(root, joinMap).ifPresent(predicateList::add);
        addScheduleTransportationExcludeIdFilter(root, cb).ifPresent(predicateList::add);

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
        Collection<ScheduleTransportationStateEnum> scheduleTransportationStates)
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

    public TripAssignmentSpecification withDriverIds(Collection<UUID> driverIds) {
        this.criteria.setDriverIds(driverIds);
        return this;
    }

    private Optional<Predicate> addDriverIdsFilter(Root<TripAssignment> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getDriverIds())
            .map(driverIds -> root.get("driverId")
                .in(driverIds));
    }

    public TripAssignmentSpecification withVehicleId(UUID vehicleId) {
        this.criteria.setVehicleId(vehicleId);
        return this;
    }

    private Optional<Predicate> addVehicleIdFilter(Root<TripAssignment> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getVehicleId())
            .map(vehicleId -> cb.equal(root.get("vehicleId"), vehicleId));
    }

    public TripAssignmentSpecification withVehicleIds(Collection<UUID> vehicleIds) {
        this.criteria.setVehicleIds(vehicleIds);
        return this;
    }

    private Optional<Predicate> addVehicleIdsFilter(Root<TripAssignment> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getVehicleIds())
            .map(vehicleIds -> root.get("vehicleId")
                .in(vehicleIds));
    }

    public TripAssignmentSpecification withTripStates(Collection<TripStateEnum> tripStates) {
        this.criteria.setTripStates(tripStates);
        return this;
    }

    private Optional<Predicate> addTripStatesFilter(Root<TripAssignment> root, CriteriaBuilder cb,
        Map<TripAssignmentSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getTripStates())
            .map(tripStates -> getOrCreateTripStateJoin(joinMap, root).get("code")
                .in(tripStates.stream()
                    .map(String::valueOf)
                    .toList()));
    }

    public TripAssignmentSpecification withExistingTripStates(Collection<TripStateEnum> tripStates)
    {
        this.criteria.setExistingTripStates(tripStates);
        return this;
    }

    private Optional<Predicate> addExistingTripStatesFilter(Root<TripAssignment> root,
        CriteriaBuilder cb, Map<TripAssignmentSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getExistingTripStates())
            .map(tripStates -> cb.or(
                cb.isNull(getOrCreateTripJoin(joinMap, root).get("id")), getOrCreateTripStateJoin(
                    joinMap, root).get("code")
                    .in(tripStates.stream()
                        .map(String::valueOf)
                        .toList())
            ));
    }

    public TripAssignmentSpecification withGreaterThanOrEqualDate(ZonedDateTime dateTime) {
        this.criteria.setGreaterThanOrEqualDate(dateTime);
        return this;
    }

    private Optional<Predicate> addGreaterThanOrEqualDateFilter(Root<TripAssignment> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getGreaterThanOrEqualDate())
            .map(dateTime -> {
                ZonedDateTime dateTimeNormalized = dateTime.withZoneSameInstant(ZoneOffset.UTC);
                return cb.greaterThanOrEqualTo(root.get("estimatedStartTime"), dateTimeNormalized);
            });
    }

    public TripAssignmentSpecification withAvailabilityValidation(ZonedDateTime availabilityFrom,
        ZonedDateTime availabilityTo)
    {
        this.criteria.setAvailableFrom(availabilityFrom);
        this.criteria.setAvailableTo(availabilityTo);
        return this;
    }

    private Optional<Predicate> addAvailabilityValidationFilter(Root<TripAssignment> root,
        CriteriaBuilder cb)
    {
        ZonedDateTime from = criteria.getAvailableFrom();
        ZonedDateTime to = criteria.getAvailableTo();

        if (from == null || to == null) {
            return Optional.empty();
        }

        ZonedDateTime fromRegularized = from.withSecond(0)
            .withNano(0);

        ZonedDateTime toRegularized = to.withSecond(0)
            .withNano(0);

        log.info("from regularized: {}", fromRegularized);
        log.info("to regularized: {}", toRegularized);

        return Optional.of(
            cb.and(
                cb.lessThan(root.get("estimatedStartTime"), toRegularized),
                cb.greaterThan(root.get("estimatedEndTime"), fromRegularized)
            ));
    }

    public TripAssignmentSpecification withTripAssignmentStates(
        Collection<TripAssignmentStateCodeEnum> tripAssignmentStates)
    {
        this.criteria.setTripAssignmentStates(tripAssignmentStates);
        return this;
    }

    private Optional<Predicate> addTripAssignmentStatesFilter(Root<TripAssignment> root,
        Map<TripAssignmentSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getTripAssignmentStates())
            .map(tripAssignmentStates -> getOrCreateTripAssignmentStateJoin(joinMap, root).get(
                    "code")
                .in(tripAssignmentStates.stream()
                    .map(String::valueOf)
                    .toList()));
    }

    public TripAssignmentSpecification withScheduleTransportationExcludeId(
        UUID scheduleTransportationExcludeId)
    {
        this.criteria.setScheduleTransportationExcludeId(scheduleTransportationExcludeId);
        return this;
    }

    private Optional<Predicate> addScheduleTransportationExcludeIdFilter(Root<TripAssignment> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getScheduleTransportationExcludeId())
            .map(
                scheduleTransportationExcludeId -> cb.notEqual(
                    root.get("scheduleTransportationId"),
                    scheduleTransportationExcludeId
                ));
    }
}
