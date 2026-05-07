package com.kernotec.driverschedule.service.rest.dto.notification.response;

import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverschedule.service.jpa.enums.notification.PersonNotificationState;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PersonNotificationCountResponse extends EntityResponse {

    private PersonNotificationState[] personNotificationStates;
    private Long count;
}
