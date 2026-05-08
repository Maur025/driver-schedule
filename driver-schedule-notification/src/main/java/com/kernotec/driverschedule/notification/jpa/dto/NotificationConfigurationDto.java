package com.kernotec.driverschedule.notification.jpa.dto;

import com.kernotec.core.jpa.dto.EntityDto;
import com.kernotec.driverschedule.notification.jpa.enums.PlatformEnum;
import com.kernotec.driverschedule.person.jpa.dto.PersonDto;
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
