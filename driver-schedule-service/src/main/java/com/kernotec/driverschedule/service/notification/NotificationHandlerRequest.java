package com.kernotec.driverschedule.service.notification;

import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;
import lombok.Builder;

@Builder
public record NotificationHandlerRequest(@NotNull Set<String> tokens, @NotNull String title,
                                         @NotNull String body, Map<String, String> dataMap)
{

}
