package com.kernotec.driverscheduleservice.rest.mapper.notification.response;

import com.kernotec.driverscheduleservice.jpa.entity.notification.NotificationConfiguration;
import com.kernotec.driverscheduleservice.rest.dto.notification.response.NotificationConfigurationResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.person.PersonResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = PersonResponseFlatMapper.class)
public interface NotificationConfigurationResponseMapper {

    NotificationConfigurationResponse toResponse(
        NotificationConfiguration notificationConfiguration);

    NotificationConfigurationResponse toResponse(UUID id);

    List<NotificationConfigurationResponse> toResponse(
        List<NotificationConfiguration> notificationConfigurationList);

    Set<NotificationConfigurationResponse> toResponse(
        Set<NotificationConfiguration> notificationConfigurationSet);
}
