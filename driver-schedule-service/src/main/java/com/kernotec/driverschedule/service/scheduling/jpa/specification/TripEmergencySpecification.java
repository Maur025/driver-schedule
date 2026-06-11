package com.kernotec.driverschedule.service.scheduling.jpa.specification;

import com.kernotec.driverschedule.common.util.CommonUtil;
import com.kernotec.driverschedule.service.scheduling.common.util.CommonSpecification;
import com.kernotec.driverschedule.service.scheduling.jpa.criteria.TripEmergencySpecificationCriteria;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripEmergency;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.TripEmergencyStateEnum;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public record TripEmergencySpecification(TripEmergencySpecificationCriteria criteria) implements
    Specification<TripEmergency>
{

    public static TripEmergencySpecification builder() {
        return new TripEmergencySpecification(new TripEmergencySpecificationCriteria());
    }

    private Join<?, ?> getOrCreateTripEmergencyStateJoin(
        Map<TripEmergencySpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(TripEmergencySpecificationJoinEnum.TRIP_EMERGENCY_STATE_JOIN)) {
            joinMap.put(
                TripEmergencySpecificationJoinEnum.TRIP_EMERGENCY_STATE_JOIN,
                root.join("tripEmergencyState", JoinType.INNER)
            );
        }

        return joinMap.get(TripEmergencySpecificationJoinEnum.TRIP_EMERGENCY_STATE_JOIN);
    }

    private Join<?, ?> getOrCreateScheduleTransportationJoin(
        Map<TripEmergencySpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(TripEmergencySpecificationJoinEnum.SCHEDULE_TRANSPORTATION_JOIN)) {
            joinMap.put(
                TripEmergencySpecificationJoinEnum.SCHEDULE_TRANSPORTATION_JOIN,
                root.join("scheduleTransportation", JoinType.INNER)
            );
        }

        return joinMap.get(TripEmergencySpecificationJoinEnum.SCHEDULE_TRANSPORTATION_JOIN);
    }

    private Join<?, ?> getOrCreateTransportationRequestJoin(
        Map<TripEmergencySpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(TripEmergencySpecificationJoinEnum.TRANSPORTATION_REQUEST_JOIN)) {
            joinMap.put(
                TripEmergencySpecificationJoinEnum.TRANSPORTATION_REQUEST_JOIN,
                getOrCreateScheduleTransportationJoin(joinMap, root).join(
                    "transportationRequest",
                    JoinType.INNER
                )
            );
        }

        return joinMap.get(TripEmergencySpecificationJoinEnum.TRANSPORTATION_REQUEST_JOIN);
    }

    @Override
    public Predicate toPredicate(Root<TripEmergency> root, CriteriaQuery<?> query,
        CriteriaBuilder cb)
    {
        List<Predicate> predicateList = new ArrayList<>();
        Map<TripEmergencySpecificationJoinEnum, Join<?, ?>> joinMap = new HashMap<>();

        addTripEmergencyStatesFilter(root, joinMap).ifPresent(predicateList::add);
        addPersonEmergencyReportedIdFilter(root, cb).ifPresent(predicateList::add);
        addTripIdFilter(root, cb).ifPresent(predicateList::add);
        addKeywordFilter(root, cb, joinMap).ifPresent(predicateList::add);

        CommonSpecification.addSimpleDateFilter(root, cb, criteria, () -> root.get("createdAt"))
            .ifPresent(predicateList::add);

        CommonSpecification.addDateRangeFilter(root, cb, criteria, () -> root.get("createdAt"))
            .ifPresent(predicateList::add);

        CommonSpecification.addMonthDateFilter(root, cb, criteria, () -> root.get("createdAt"))
            .ifPresent(predicateList::add);

        CommonSpecification.addYearDateFilter(root, cb, criteria, () -> root.get("createdAt"))
            .ifPresent(predicateList::add);

        query.distinct(true);
        return cb.and(predicateList.toArray(Predicate[]::new));
    }

    public TripEmergencySpecification withTripEmergencyStates(
        Collection<TripEmergencyStateEnum> tripEmergencyStates)
    {
        this.criteria.setTripEmergencyStates(tripEmergencyStates);
        return this;
    }

    private Optional<Predicate> addTripEmergencyStatesFilter(Root<TripEmergency> root,
        Map<TripEmergencySpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getTripEmergencyStates())
            .map(tripEmergencyStates -> getOrCreateTripEmergencyStateJoin(joinMap, root).get("code")
                .in(tripEmergencyStates.stream()
                    .map(String::valueOf)
                    .toList()));
    }

    public TripEmergencySpecification withPersonEmergencyReportedId(UUID personEmergencyReportedId)
    {
        this.criteria.setPersonEmergencyReportedId(personEmergencyReportedId);
        return this;
    }

    private Optional<Predicate> addPersonEmergencyReportedIdFilter(Root<TripEmergency> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getPersonEmergencyReportedId())
            .map(personEmergencyReportedId -> cb.equal(
                root.get("personEmergencyReportedId"),
                personEmergencyReportedId
            ));
    }

    public TripEmergencySpecification withTripId(UUID tripId) {
        this.criteria.setTripId(tripId);
        return this;
    }

    private Optional<Predicate> addTripIdFilter(Root<TripEmergency> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getTripId())
            .map(tripId -> cb.equal(root.get("tripId"), tripId));
    }

    public TripEmergencySpecification withKeyword(String keyword) {
        this.criteria.setKeyword(CommonUtil.getSafeString(keyword));
        return this;
    }

    private Optional<Predicate> addKeywordFilter(Root<TripEmergency> root, CriteriaBuilder cb,
        Map<TripEmergencySpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getKeyword())
            .map(keyword -> {
                String pattern = "%" + keyword.toLowerCase() + "%";
                Join<?, ?> requestJoin = getOrCreateTransportationRequestJoin(joinMap, root);

                return cb.or(
                    cb.like(cb.lower(requestJoin.get("code")), pattern),
                    cb.like(
                        requestJoin.get("correlative")
                            .as(String.class), pattern
                    )
                );
            });
    }

    public TripEmergencySpecification withZoneId(String zoneId) {
        this.criteria.setZoneId(zoneId);
        return this;
    }

    public TripEmergencySpecification withSimpleDate(ZonedDateTime simpleDate) {
        this.criteria.setSimpleDate(simpleDate);
        return this;
    }

    public TripEmergencySpecification withDateRange(ZonedDateTime fromDate, ZonedDateTime toDate)
    {
        this.criteria.setFromDate(fromDate);
        this.criteria.setToDate(toDate);
        return this;
    }

    public TripEmergencySpecification withMonthDate(ZonedDateTime monthDate) {
        this.criteria.setMonthDate(monthDate);
        return this;
    }

    public TripEmergencySpecification withYearDate(ZonedDateTime yearDate) {
        this.criteria.setYearDate(yearDate);
        return this;
    }
}
