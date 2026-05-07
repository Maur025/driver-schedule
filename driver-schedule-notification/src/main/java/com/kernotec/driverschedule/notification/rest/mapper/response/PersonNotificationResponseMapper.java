package com.kernotec.driverschedule.notification.rest.mapper.response;

import com.kernotec.driverschedule.notification.jpa.entitiy.PersonNotification;
import com.kernotec.driverschedule.notification.rest.dto.response.PersonNotificationResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PersonNotificationResponseMapper {

    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "person", ignore = true)
    PersonNotificationResponse toResponse(PersonNotification personNotification);

    List<PersonNotificationResponse> toResponse(List<PersonNotification> personNotificationList);

    Set<PersonNotificationResponse> toResponse(Set<PersonNotification> personNotificationSet);
}
