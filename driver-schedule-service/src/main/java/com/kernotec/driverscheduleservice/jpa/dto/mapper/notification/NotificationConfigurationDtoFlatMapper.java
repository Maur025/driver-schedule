package com.kernotec.driverscheduleservice.jpa.dto.mapper.notification;

import com.kernotec.driverscheduleservice.jpa.dto.notification.NotificationConfigurationDto;
import com.kernotec.driverscheduleservice.jpa.entity.notification.NotificationConfiguration;
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
