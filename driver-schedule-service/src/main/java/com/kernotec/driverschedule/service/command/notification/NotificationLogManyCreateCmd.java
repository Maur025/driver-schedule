package com.kernotec.driverschedule.service.command.notification;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.jpa.entity.notification.NotificationLog;
import com.kernotec.driverschedule.service.jpa.service.notification.NotificationLogService;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationLogManyCreateCmd extends
    AbstractTransactionalRequiredCommand<NotificationLogManyCreateCmd.Request, List<NotificationLog>>
{

    private final NotificationLogService notificationLogService;

    @Override
    protected List<NotificationLog> run(Request request) {
        if (request.notificationLogList()
            .isEmpty())
        {
            log.debug("Notification log empty... nothing to save");
            return null;
        }

        return notificationLogService.saveAll(request.notificationLogList());
    }

    @Builder
    public record Request(@NonNull List<NotificationLog> notificationLogList) {

    }
}