package com.kernotec.driverscheduleservice.jpa.specification.schedule.transportation;

import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.specification.criteria.ScheduleTransportationSpecificationCriteria;
import com.kernotec.driverscheduleservice.util.ZonedDateTimeUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public Predicate toPredicate(Root<ScheduleTransportation> root, CriteriaQuery<?> query,
        CriteriaBuilder cb)
    {
        List<Predicate> predicateList = new ArrayList<>();

        addConflictValidationFilter(root, cb).ifPresent(predicateList::add);
        addVehicleIdFilter(root, cb).ifPresent(predicateList::add);
        addDriverIdFilter(root, cb).ifPresent(predicateList::add);
        addTransportationRequestIdFilter(root, cb).ifPresent(predicateList::add);
        addScheduleTransportationExcludeIdFilter(root, cb).ifPresent(predicateList::add);

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
}
