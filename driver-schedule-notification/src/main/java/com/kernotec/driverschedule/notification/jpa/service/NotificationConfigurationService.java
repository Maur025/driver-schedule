package com.kernotec.driverschedule.notification.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.common.util.CommonUtil;
import com.kernotec.driverschedule.notification.jpa.dto.NotificationConfigurationDto;
import com.kernotec.driverschedule.notification.jpa.entitiy.NotificationConfiguration;
import com.kernotec.driverschedule.notification.jpa.mapper.NotificationConfigurationDtoFlatMapper;
import com.kernotec.driverschedule.notification.jpa.repository.NotificationConfigurationRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class NotificationConfigurationService extends
    BaseServiceImpl<NotificationConfiguration, UUID>
{

    private final NotificationConfigurationRepository repository;
    private final NotificationConfigurationDtoFlatMapper notificationConfigurationDtoFlatMapper;

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

    public List<NotificationConfiguration> findByPersonIdInAndActivedAndDeleted(
        Collection<UUID> personIds, boolean actived, boolean deleted)
    {
        return repository.findByPersonIdInAndActivedAndDeleted(personIds, actived, deleted);
    }

    public List<NotificationConfiguration> findByPersonIdInAndActived(Collection<UUID> personIds,
        boolean actived)
    {
        return findByPersonIdInAndActivedAndDeleted(personIds, actived, false);
    }

    public List<NotificationConfiguration> findByPersonIdInForNotification(
        Collection<UUID> personIds)
    {
        return findByPersonIdInAndActived(personIds, true);
    }

    public List<NotificationConfigurationDto> findDtoByPersonIdInForNotification(
        Collection<UUID> personIds)
    {
        List<NotificationConfiguration> notificationConfigurationList = findByPersonIdInForNotification(
            personIds);

        return notificationConfigurationDtoFlatMapper.toDto(notificationConfigurationList);
    }

    @Transactional
    public void deleteAllByIdIn(Collection<UUID> ids) {
        repository.deleteAllByIdIn(ids);
    }
}
