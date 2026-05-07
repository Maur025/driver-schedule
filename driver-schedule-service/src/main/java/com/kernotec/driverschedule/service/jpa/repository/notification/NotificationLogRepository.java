package com.kernotec.driverschedule.service.jpa.repository.notification;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.jpa.entity.notification.NotificationLog;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogRepository extends BaseRepository<NotificationLog, UUID> {

}
