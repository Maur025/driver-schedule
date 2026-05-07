package com.kernotec.driverschedule.service.jpa.dto.notification;

import com.kernotec.core.jpa.dto.EntityDto;
import com.kernotec.driverschedule.service.jpa.dto.resource.PersonDto;
import com.kernotec.driverschedule.service.jpa.enums.notification.PlatformEnum;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationConfigurationDto extends EntityDto {

    private UUID userId;
    private String deviceId;
    private PlatformEnum platform;
    private String token;
    private boolean actived;

    private UUID personId;
    private PersonDto person;
}
