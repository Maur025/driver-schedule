package com.kernotec.driverschedule.notification.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.notification.jpa.entitiy.PersonNotification;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonNotificationRepository extends BaseRepository<PersonNotification, UUID> {

}
