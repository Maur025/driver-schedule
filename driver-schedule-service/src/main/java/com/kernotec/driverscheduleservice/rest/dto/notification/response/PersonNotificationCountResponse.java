package com.kernotec.driverscheduleservice.rest.dto.notification.response;

import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverscheduleservice.jpa.enums.notification.PersonNotificationState;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PersonNotificationCountResponse extends EntityResponse {

    private PersonNotificationState[] personNotificationStates;
    private Long count;
}
