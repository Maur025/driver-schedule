package com.kernotec.driverscheduleservice.rest.mapper.notification.request;

import com.kernotec.driverscheduleservice.jpa.dto.notification.NotificationConfigurationDto;
import com.kernotec.driverscheduleservice.jpa.entity.notification.NotificationLog;
import com.kernotec.driverscheduleservice.jpa.enums.notification.NotificationLogStateEnum;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface NotificationLogEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "notificationCampaign", ignore = true)
    NotificationLog toEntity(NotificationConfigurationDto notificationConfigurationDto,
        NotificationLogStateEnum notificationLogState, ZonedDateTime sentAt,
        UUID notificationCampaignId);

    default List<NotificationLog> toEntity(List<NotificationConfigurationDto> dtoList,
        NotificationLogStateEnum notificationLogState, ZonedDateTime sentAt,
        UUID notificationCampaignId)
    {
        if (dtoList == null) {
            return null;
        }

        List<NotificationLog> list = new ArrayList<>(dtoList.size());

        for (NotificationConfigurationDto dto : dtoList) {
            list.add(toEntity(dto, notificationLogState, sentAt, notificationCampaignId));
        }

        return list;
    }
}
