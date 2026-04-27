package com.kernotec.driverscheduleservice.jpa.service.notification;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.notification.NotificationConfiguration;
import com.kernotec.driverscheduleservice.jpa.repository.notification.NotificationConfigurationRepository;
import com.kernotec.driverscheduleservice.util.CommonUtil;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationConfigurationService extends
    BaseServiceImpl<NotificationConfiguration, UUID>
{

    private final NotificationConfigurationRepository repository;

    @Override
    protected String resourceName() {
        return "Notification Configuration";
    }

    @Override
    protected BaseRepository<NotificationConfiguration, UUID> repository() {
        return repository;
    }


    public Optional<NotificationConfiguration> findByTokenAndDeleted(String token, boolean deleted)
    {
        String tokenSafe = CommonUtil.getSafeString(token);
        return repository.findByTokenAndDeleted(tokenSafe, deleted);
    }

    public Optional<NotificationConfiguration> findByToken(String token)
    {
        return findByTokenAndDeleted(token, false);
    }
}
