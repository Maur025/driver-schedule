package com.kernotec.driverschedule.service.jpa.service.notification;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.jpa.entity.notification.NotificationLog;
import com.kernotec.driverschedule.service.jpa.repository.notification.NotificationLogRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationLogService extends BaseServiceImpl<NotificationLog, UUID> {

    private final NotificationLogRepository repository;

    @Override
    protected String resourceName() {
        return "Notification Log";
    }

    @Override
    protected BaseRepository<NotificationLog, UUID> repository() {
        return repository;
    }
}
