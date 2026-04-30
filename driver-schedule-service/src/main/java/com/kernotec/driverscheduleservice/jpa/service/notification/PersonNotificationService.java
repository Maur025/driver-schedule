package com.kernotec.driverscheduleservice.jpa.service.notification;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.notification.PersonNotification;
import com.kernotec.driverscheduleservice.jpa.repository.notification.PersonNotificationRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PersonNotificationService extends BaseServiceImpl<PersonNotification, UUID> {

    private final PersonNotificationRepository repository;

    @Override
    protected String resourceName() {
        return "Person Notification";
    }

    @Override
    protected BaseRepository<PersonNotification, UUID> repository() {
        return repository;
    }
}
