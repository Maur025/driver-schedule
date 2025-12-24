package com.kernotec.driverscheduleservice.jpa.specification.transportation.request;

import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.TripTypeEnum;
import com.kernotec.driverscheduleservice.jpa.specification.criteria.TransportationRequestSpecificationCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

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

}
