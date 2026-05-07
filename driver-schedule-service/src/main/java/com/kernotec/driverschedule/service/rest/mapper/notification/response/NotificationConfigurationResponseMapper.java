package com.kernotec.driverschedule.service.rest.mapper.notification.response;

import com.kernotec.driverschedule.service.jpa.entity.notification.NotificationConfiguration;
import com.kernotec.driverschedule.service.rest.dto.notification.response.NotificationConfigurationResponse;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.person.PersonResponseFlatMapper;
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
