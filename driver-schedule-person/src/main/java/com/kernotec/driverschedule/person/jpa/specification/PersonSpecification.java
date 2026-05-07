package com.kernotec.driverschedule.person.jpa.specification;

import com.kernotec.driverschedule.person.jpa.criteria.PersonSpecificationCriteria;
import com.kernotec.driverschedule.person.jpa.entity.Person;
import com.kernotec.driverschedule.person.jpa.enums.PersonTypeEnum;
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
import org.springframework.data.jpa.domain.Specification;

public record PersonSpecification(PersonSpecificationCriteria criteria) implements
    Specification<Person>
{

    public static PersonSpecification builder() {
        return new PersonSpecification(new PersonSpecificationCriteria());
    }

    private Join<?, ?> getOrCreatePersonTypeJoin(
        Map<PersonSpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(PersonSpecificationJoinEnum.PERSON_TYPE_JOIN)) {
            joinMap.put(
                PersonSpecificationJoinEnum.PERSON_TYPE_JOIN,
                root.join("personTypes", JoinType.INNER)
            );
        }

        return joinMap.get(PersonSpecificationJoinEnum.PERSON_TYPE_JOIN);
    }

    @Override
    public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb)
    {
        List<Predicate> predicateList = new ArrayList<>();
        Map<PersonSpecificationJoinEnum, Join<?, ?>> joinMap = new HashMap<>();

        addPersonTypeFilter(root, cb, joinMap).ifPresent(predicateList::add);

        query.distinct(true);
        return cb.and(predicateList.toArray(Predicate[]::new));
    }

    public PersonSpecification withPersonType(PersonTypeEnum personType) {
        this.criteria.setPersonType(personType);
        return this;
    }

    private Optional<Predicate> addPersonTypeFilter(Root<Person> root, CriteriaBuilder cb,
        Map<PersonSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getPersonType())
            .map(personType -> cb.equal(
                getOrCreatePersonTypeJoin(joinMap, root).get("code"),
                String.valueOf(personType)
            ));
    }
}
