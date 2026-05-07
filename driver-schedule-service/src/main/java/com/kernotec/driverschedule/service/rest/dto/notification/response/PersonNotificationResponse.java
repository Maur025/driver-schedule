package com.kernotec.driverschedule.service.rest.dto.notification.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverschedule.service.jpa.enums.notification.PersonNotificationState;
import com.kernotec.driverschedule.service.rest.dto.resource.response.person.PersonResponse;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class PersonNotificationResponse extends EntityResponse {

    private String title;
    private String body;
    private ZonedDateTime sentAt;
    private ZonedDateTime readAt;

    private Map<String, String> data;
    private PersonNotificationState state;

    private UUID personId;
    private PersonResponse person;
}
