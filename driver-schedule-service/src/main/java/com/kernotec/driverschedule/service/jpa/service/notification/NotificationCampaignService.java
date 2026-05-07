package com.kernotec.driverschedule.service.jpa.service.notification;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.jpa.entity.notification.NotificationCampaign;
import com.kernotec.driverschedule.service.jpa.repository.notification.NotificationCampaignRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationCampaignService extends BaseServiceImpl<NotificationCampaign, UUID> {

    private final NotificationCampaignRepository repository;

    @Override
    protected String resourceName() {
        return "Notification Campaign";
    }

    @Override
    protected BaseRepository<NotificationCampaign, UUID> repository() {
        return repository;
    }
}
