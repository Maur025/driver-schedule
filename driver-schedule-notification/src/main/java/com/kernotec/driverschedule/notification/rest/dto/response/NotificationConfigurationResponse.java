package com.kernotec.driverschedule.notification.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverschedule.notification.jpa.enums.PlatformEnum;
import com.kernotec.driverschedule.person.rest.dto.response.PersonResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class NotificationConfigurationResponse extends EntityResponse {

    private UUID userId;
    private String deviceId;
    private PlatformEnum platform;
    private String token;
    private boolean actived;

    private UUID personId;
    private PersonResponse person;
}
