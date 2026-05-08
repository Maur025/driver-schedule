package com.kernotec.driverschedule.notification.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.notification.jpa.entitiy.PersonNotification;
import com.kernotec.driverschedule.notification.jpa.enums.PersonNotificationState;
import com.kernotec.driverschedule.notification.jpa.repository.PersonNotificationRepository;
import com.kernotec.driverschedule.notification.jpa.specification.PersonNotificationSpecification;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PersonNotificationService extends BaseServiceImpl<PersonNotification, UUID> {

    private final PersonNotificationRepository repository;
    private final PersonService personService;

    @Override
    protected String resourceName() {
        return "Person Notification";
    }

    @Override
    protected BaseRepository<PersonNotification, UUID> repository() {
        return repository;
    }

    public Page<PersonNotification> findAllBySearch(Set<PersonNotificationState> notificationStates,
        Pageable pageable)
    {
        UUID personId = personService.findIdByUserIdAuthenticateThrow();

        return repository.findAll(
            PersonNotificationSpecification.builder()
                .withDeleted(false)
                .withStates(notificationStates)
                .withPersonId(personId), pageable
        );
    }

    public Long countAllBySearch(Set<PersonNotificationState> notificationStates) {
        UUID personId = personService.findIdByUserIdAuthenticateThrow();

        return repository.count(PersonNotificationSpecification.builder()
            .withDeleted(false)
            .withStates(notificationStates)
            .withPersonId(personId));
    }
}
