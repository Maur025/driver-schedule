package com.kernotec.driverschedule.service.notification.dto;

import com.kernotec.driverschedule.service.jpa.dto.notification.NotificationConfigurationDto;
import com.kernotec.driverschedule.service.notification.NotificationHandlerResponse;
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
