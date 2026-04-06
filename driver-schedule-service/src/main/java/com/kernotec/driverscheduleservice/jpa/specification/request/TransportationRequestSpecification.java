package com.kernotec.driverscheduleservice.jpa.specification.request;

import com.kernotec.driverscheduleservice.jpa.entity.request.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.enums.request.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.request.TripTypeEnum;
import com.kernotec.driverscheduleservice.jpa.specification.request.criteria.TransportationRequestSpecificationCriteria;
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
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public record TransportationRequestSpecification(
    TransportationRequestSpecificationCriteria criteria) implements
    Specification<TransportationRequest>
{

    public static TransportationRequestSpecification builder() {
        return new TransportationRequestSpecification(
            new TransportationRequestSpecificationCriteria());
    }

    private Join<?, ?> getOrCreateTransportationRequestStateJoin(
        Map<TransportationRequestSpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(
            TransportationRequestSpecificationJoinEnum.TRANSPORTATION_REQUEST_STATE_JOIN))
        {
            joinMap.put(
                TransportationRequestSpecificationJoinEnum.TRANSPORTATION_REQUEST_STATE_JOIN,
                root.join("transportationRequestState", JoinType.INNER)
            );
        }

        return joinMap.get(
            TransportationRequestSpecificationJoinEnum.TRANSPORTATION_REQUEST_STATE_JOIN);
    }

    @Override
    public Predicate toPredicate(Root<TransportationRequest> root, CriteriaQuery<?> query,
        CriteriaBuilder cb)
    {
        List<Predicate> predicateList = new ArrayList<>();
        Map<TransportationRequestSpecificationJoinEnum, Join<?, ?>> joinMap = new HashMap<>();

        addUserIdFilter(root, cb).ifPresent(predicateList::add);
        addTransportationRequestStateIdFilter(root, cb).ifPresent(predicateList::add);
        addPersonRequestedIdFilter(root, cb).ifPresent(predicateList::add);
        addTripTypeFilter(root, cb).ifPresent(predicateList::add);
        addTransportationRequestStateFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addSimpleDateFilter(root, cb).ifPresent(predicateList::add);
        addDateRangeFilter(root, cb).ifPresent(predicateList::add);
        addMonthDateFilter(root, cb).ifPresent(predicateList::add);
        addYearDateFilter(root, cb).ifPresent(predicateList::add);
        addOnlyRecordsOfPersonIdFilter(root, cb).ifPresent(predicateList::add);
        addKeywordFilter(root, cb).ifPresent(predicateList::add);

        query.distinct(true);
        return cb.and(predicateList.toArray(Predicate[]::new));
    }

    public TransportationRequestSpecification withUserId(String userId) {
        this.criteria.setUserId(userId);
        return this;
    }

    private Optional<Predicate> addUserIdFilter(Root<TransportationRequest> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getUserId())
            .map(userId -> cb.equal(root.get("createdBy"), userId));
    }

    public TransportationRequestSpecification withTransportationRequestStateId(
        UUID transportationRequestStateId)
    {
        this.criteria.setTransportationRequestStateId(transportationRequestStateId);
        return this;
    }

    private Optional<Predicate> addTransportationRequestStateIdFilter(
        Root<TransportationRequest> root, CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getTransportationRequestStateId())
            .map(transportationRequestStateId -> cb.equal(
                root.get("transportationRequestStateId"),
                transportationRequestStateId
            ));
    }

    public TransportationRequestSpecification withPersonRequestedId(UUID personRequestedId) {
        this.criteria.setPersonRequestedId(personRequestedId);
        return this;
    }

    private Optional<Predicate> addPersonRequestedIdFilter(Root<TransportationRequest> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getPersonRequestedId())
            .map(personRequestId -> cb.equal(root.get("personRequestedId"), personRequestId));
    }

    public TransportationRequestSpecification withTripType(TripTypeEnum tripType) {
        this.criteria.setTripType(tripType);
        return this;
    }

    private Optional<Predicate> addTripTypeFilter(Root<TransportationRequest> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getTripType())
            .map(tripType -> cb.equal(root.get("tripType"), tripType));
    }

    public TransportationRequestSpecification withTransportationRequestState(
        TransportationRequestStateEnum transportationRequestState)
    {
        this.criteria.setTransportationRequestState(transportationRequestState);
        return this;
    }

    private Optional<Predicate> addTransportationRequestStateFilter(
        Root<TransportationRequest> root, CriteriaBuilder cb,
        Map<TransportationRequestSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getTransportationRequestState())
            .map(
                transportationRequestState -> cb.equal(
                    getOrCreateTransportationRequestStateJoin(joinMap, root).get("code"),
                    String.valueOf(transportationRequestState)
                ));
    }

    public TransportationRequestSpecification withZoneId(String zoneId) {
        this.criteria.setZoneId(zoneId);
        return this;
    }

    public TransportationRequestSpecification withSimpleDate(ZonedDateTime simpleDate) {
        this.criteria.setSimpleDate(simpleDate);
        return this;
    }

    private Optional<Predicate> addSimpleDateFilter(Root<TransportationRequest> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getSimpleDate())
            .map(
                simpleDate -> CommonSpecification.simpleDatePredicate(
                    cb, root.get("requestedFrom"), simpleDate, criteria.getZoneId()));
    }

    public TransportationRequestSpecification withDateRange(ZonedDateTime fromDate,
        ZonedDateTime toDate)
    {
        this.criteria.setFromDate(fromDate);
        this.criteria.setToDate(toDate);
        return this;
    }

    private Optional<Predicate> addDateRangeFilter(Root<TransportationRequest> root,
        CriteriaBuilder cb)
    {
        ZonedDateTime from = criteria.getFromDate();
        ZonedDateTime to = criteria.getToDate();

        if (from != null && to != null) {
            return Optional.of(
                CommonSpecification.dateRangePredicate(
                    cb, root.get("requestedFrom"), from, to,
                    criteria.getZoneId()
                ));
        }

        return Optional.empty();
    }

    public TransportationRequestSpecification withMonthDate(ZonedDateTime monthDate) {
        this.criteria.setMonthDate(monthDate);
        return this;
    }

    private Optional<Predicate> addMonthDateFilter(Root<TransportationRequest> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getMonthDate())
            .map(monthDate -> CommonSpecification.monthDatePredicate(
                cb, root.get("requestedFrom"),
                monthDate, criteria.getZoneId()
            ));
    }

    public TransportationRequestSpecification withYearDate(ZonedDateTime yearDate) {
        this.criteria.setYearDate(yearDate);
        return this;
    }

    private Optional<Predicate> addYearDateFilter(Root<TransportationRequest> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getYearDate())
            .map(yearDate -> CommonSpecification.yearDatePredicate(
                cb, root.get("requestedFrom"),
                yearDate, criteria.getZoneId()
            ));
    }

    public TransportationRequestSpecification withOnlyRecordsOfPersonId(UUID personId) {
        this.criteria.setOnlyRecordsOfPersonId(personId);
        return this;
    }

    private Optional<Predicate> addOnlyRecordsOfPersonIdFilter(Root<TransportationRequest> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getOnlyRecordsOfPersonId())
            .map(onlyRecordsOfPersonId -> cb.equal(
                root.get("personRequestedId"),
                onlyRecordsOfPersonId
            ));
    }

    public TransportationRequestSpecification withKeyword(String keyword) {
        this.criteria.setKeyword(keyword);
        return this;
    }

    private Optional<Predicate> addKeywordFilter(Root<TransportationRequest> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getKeyword())
            .map(keyword -> {
                String pattern = "%" + keyword.toLowerCase() + "%";

                return cb.or(
                    cb.like(cb.lower(root.get("code")), pattern), cb.like(
                        root.get("correlative")
                            .as(String.class), pattern
                    )
                );
            });
    }
}
