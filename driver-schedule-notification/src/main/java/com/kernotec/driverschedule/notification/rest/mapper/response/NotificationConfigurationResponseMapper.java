package com.kernotec.driverschedule.notification.rest.mapper.response;

import com.kernotec.driverschedule.notification.jpa.entitiy.NotificationConfiguration;
import com.kernotec.driverschedule.notification.rest.dto.response.NotificationConfigurationResponse;
import com.kernotec.driverschedule.person.rest.mapper.response.PersonResponseFlatMapper;
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
