package com.kernotec.driverscheduleservice.jpa.repository.notification;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.notification.NotificationConfiguration;
import java.util.Optional;
import java.util.UUID;

public interface NotificationConfigurationRepository extends
    BaseRepository<NotificationConfiguration, UUID>
{

    Optional<NotificationConfiguration> findByTokenAndDeleted(String token, boolean deleted);

}
