package com.kernotec.driverscheduleservice.jpa.specification.location;

import com.kernotec.driverscheduleservice.jpa.entity.Location;
import com.kernotec.driverscheduleservice.jpa.specification.criteria.LocationSpecificationCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.data.jpa.domain.Specification;

public record LocationSpecification(LocationSpecificationCriteria criteria) implements
    Specification<Location>
{

    public static LocationSpecification builder() {
        return new LocationSpecification(new LocationSpecificationCriteria());
    }

    @Override
    public @Nullable Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query,
        CriteriaBuilder cb)
    {
        List<Predicate> predicateList = new ArrayList<>();

        addKeywordFilter(root, cb).ifPresent(predicateList::add);

        query.distinct(true);
        return cb.and(predicateList.toArray(Predicate[]::new));
    }

    public LocationSpecification withKeyword(String keyword) {
        this.criteria.setKeyword(keyword);
        return this;
    }

    private Optional<Predicate> addKeywordFilter(Root<Location> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getKeyword())
            .map(keyword -> {
                String pattern = "%" + keyword.toLowerCase() + "%";

                return cb.and(cb.like(cb.lower(root.get("name")), pattern));
            });
    }
}
