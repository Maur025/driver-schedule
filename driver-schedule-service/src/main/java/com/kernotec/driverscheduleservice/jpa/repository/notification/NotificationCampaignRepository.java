package com.kernotec.driverscheduleservice.jpa.repository.notification;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.notification.NotificationCampaign;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationCampaignRepository extends BaseRepository<NotificationCampaign, UUID> {

}
