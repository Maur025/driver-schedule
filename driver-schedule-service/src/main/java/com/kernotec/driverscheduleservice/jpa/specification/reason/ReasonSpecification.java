package com.kernotec.driverscheduleservice.jpa.specification.reason;

import com.kernotec.driverscheduleservice.jpa.entity.Reason;
import com.kernotec.driverscheduleservice.jpa.specification.criteria.ReasonSpecificationCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.data.jpa.domain.Specification;

public record ReasonSpecification(ReasonSpecificationCriteria criteria) implements
    Specification<Reason>
{

    public static ReasonSpecification builder() {
        return new ReasonSpecification(new ReasonSpecificationCriteria());
    }

    @Override
    public @Nullable Predicate toPredicate(Root<Reason> root, CriteriaQuery<?> query,
        CriteriaBuilder criteriaBuilder)
    {
        return null;
    }

    public ReasonSpecification withTransportationRequestId(UUID transportationRequestId) {
        this.criteria.setTransportationRequestId(transportationRequestId);
        return this;
    }
}
