package com.kernotec.driverscheduleservice.jpa.specification.schedule.transportation;

import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.specification.criteria.ScheduleTransportationSpecificationCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public record ScheduleTransportationSpecification(
    ScheduleTransportationSpecificationCriteria criteria) implements
    Specification<ScheduleTransportation>
{

    public static ScheduleTransportationSpecification builder() {
        return new ScheduleTransportationSpecification(
            new ScheduleTransportationSpecificationCriteria());
    }

    @Override
    public Predicate toPredicate(Root<ScheduleTransportation> root, CriteriaQuery<?> query,
        CriteriaBuilder cb)
    {
        List<Predicate> predicateList = new ArrayList<>();

        addConflictValidationFilter(root, cb).ifPresent(predicateList::add);
        addVehicleIdFilter(root, cb).ifPresent(predicateList::add);
        addDriverIdFilter(root, cb).ifPresent(predicateList::add);
        addTransportationRequestIdFilter(root, cb).ifPresent(predicateList::add);

        query.distinct(true);
        return cb.and(predicateList.toArray(Predicate[]::new));
    }

    public ScheduleTransportationSpecification withAvailabilityValidation(
        ZonedDateTime availabilityValidationFrom, ZonedDateTime availabilityValidationTo)
    {
        this.criteria.setConflictValidationFrom(availabilityValidationFrom);
        this.criteria.setConflictValidationTo(availabilityValidationTo);
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

        return Optional.of(
            cb.and(
                cb.lessThan(root.get("scheduleFrom"), to),
                cb.greaterThan(root.get("scheduleTo"), from)
            ));
    }

    public ScheduleTransportationSpecification withVehicleId(UUID vehicleId) {
        this.criteria.setVehicleId(vehicleId);
        return this;
    }

    private Optional<Predicate> addVehicleIdFilter(Root<ScheduleTransportation> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getVehicleId())
            .map(vehicleId -> cb.equal(root.get("vehicleId"), vehicleId));
    }

    public ScheduleTransportationSpecification withDriverId(UUID driverId) {
        this.criteria.setDriverId(driverId);
        return this;
    }

    private Optional<Predicate> addDriverIdFilter(Root<ScheduleTransportation> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getDriverId())
            .map(driverId -> cb.equal(root.get("driverId"), driverId));
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
}
