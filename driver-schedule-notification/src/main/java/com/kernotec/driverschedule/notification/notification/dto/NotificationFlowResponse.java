package com.kernotec.driverschedule.notification.notification.dto;

import com.kernotec.driverschedule.notification.jpa.dto.NotificationConfigurationDto;
import com.kernotec.driverschedule.notification.rest.dto.response.NotificationHandlerResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;

@Builder
public record NotificationFlowResponse(NotificationHandlerResponse notificationResponse,
                                       List<NotificationConfigurationDto> notificationConfigDtoList,
                                       Set<String> usedTokens, Set<UUID> personIds)
{

}
