package com.kernotec.driverscheduleservice.jpa.service.notification;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.notification.NotificationCampaign;
import com.kernotec.driverscheduleservice.jpa.repository.notification.NotificationCampaignRepository;
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
