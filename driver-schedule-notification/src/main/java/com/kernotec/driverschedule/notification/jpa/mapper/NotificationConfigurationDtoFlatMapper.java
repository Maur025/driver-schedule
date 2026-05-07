package com.kernotec.driverschedule.notification.jpa.mapper;

import com.kernotec.driverschedule.notification.jpa.dto.NotificationConfigurationDto;
import com.kernotec.driverschedule.notification.jpa.entitiy.NotificationConfiguration;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface NotificationConfigurationDtoFlatMapper {

    @Mapping(target = "person", ignore = true)
    NotificationConfigurationDto toDto(NotificationConfiguration notificationConfiguration);

    List<NotificationConfigurationDto> toDto(
        List<NotificationConfiguration> notificationConfigurationList);

    Set<NotificationConfigurationDto> toDto(
        Set<NotificationConfiguration> notificationConfigurationSet);
}
