package com.kernotec.driverschedule.notification.rest.dto.response;

import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverschedule.notification.jpa.enums.PersonNotificationState;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PersonNotificationCountResponse extends EntityResponse {

    private PersonNotificationState[] personNotificationStates;
    private Long count;
}
