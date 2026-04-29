package com.kernotec.driverscheduleservice.jpa.dto.notification;

import com.kernotec.core.jpa.dto.EntityDto;
import com.kernotec.driverscheduleservice.jpa.dto.resource.PersonDto;
import com.kernotec.driverscheduleservice.jpa.enums.notification.PlatformEnum;
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
