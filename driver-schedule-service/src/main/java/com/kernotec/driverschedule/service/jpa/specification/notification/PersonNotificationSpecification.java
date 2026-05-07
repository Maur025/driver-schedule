package com.kernotec.driverschedule.service.jpa.specification.notification;

import com.kernotec.driverschedule.service.jpa.entity.notification.PersonNotification;
import com.kernotec.driverschedule.service.jpa.enums.notification.PersonNotificationState;
import com.kernotec.driverschedule.service.jpa.specification.notification.criteria.PersonNotificationSpecificationCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public record PersonNotificationSpecification(
    PersonNotificationSpecificationCriteria criteria) implements Specification<PersonNotification>
{

    public static PersonNotificationSpecification builder() {
        return new PersonNotificationSpecification(new PersonNotificationSpecificationCriteria());
    }

    @Override
    public Predicate toPredicate(Root<PersonNotification> root, CriteriaQuery<?> query,
        CriteriaBuilder cb)
    {
        List<Predicate> predicateList = new ArrayList<>();

        addPersonIdFilter(root, cb).ifPresent(predicateList::add);
        addStatesFilter(root).ifPresent(predicateList::add);
        addDeletedFilter(root, cb).ifPresent(predicateList::add);

        query.distinct(true);
        return cb.and(predicateList.toArray(Predicate[]::new));
    }

    public PersonNotificationSpecification withPersonId(UUID personId) {
        this.criteria.setPersonId(personId);
        return this;
    }

    private Optional<Predicate> addPersonIdFilter(Root<PersonNotification> root, CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getPersonId())
            .map(personId -> cb.equal(root.get("personId"), personId));
    }

    public PersonNotificationSpecification withStates(Collection<PersonNotificationState> states) {
        this.criteria.setStates(states);
        return this;
    }

    private Optional<Predicate> addStatesFilter(Root<PersonNotification> root) {
        return Optional.ofNullable(criteria.getStates())
            .map(states -> root.get("state")
                .in(states));
    }

    public PersonNotificationSpecification withDeleted(Boolean deleted) {
        this.criteria.setDeleted(deleted);
        return this;
    }

    private Optional<Predicate> addDeletedFilter(Root<PersonNotification> root, CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getDeleted())
            .map(deleted -> cb.equal(root.get("deleted"), deleted));
    }
}
