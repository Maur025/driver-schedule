package com.kernotec.driverschedule.resource.jpa.specification;

import com.kernotec.driverschedule.resource.jpa.criteria.ReasonSpecificationCriteria;
import com.kernotec.driverschedule.resource.jpa.entity.Reason;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
}
