package com.kernotec.driverschedule.notification.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.driverschedule.notification.jpa.enums.PlatformEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class NotificationConfigurationCreateRequest extends BaseRequest {

    private String deviceId;
    private PlatformEnum platform;
    private String token;
}
