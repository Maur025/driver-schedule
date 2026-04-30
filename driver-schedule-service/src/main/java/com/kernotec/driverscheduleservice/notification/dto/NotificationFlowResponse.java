package com.kernotec.driverscheduleservice.notification.dto;

import com.kernotec.driverscheduleservice.jpa.dto.notification.NotificationConfigurationDto;
import com.kernotec.driverscheduleservice.notification.NotificationHandlerResponse;
import java.util.List;
import java.util.Set;
import lombok.Builder;

@Builder
public record NotificationFlowResponse(NotificationHandlerResponse notificationResponse,
                                       List<NotificationConfigurationDto> notificationConfigDtoList,
                                       Set<String> usedTokens)
{

}
