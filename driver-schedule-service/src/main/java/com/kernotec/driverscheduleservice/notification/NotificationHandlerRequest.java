package com.kernotec.driverscheduleservice.notification;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Builder;

@Builder
public record NotificationHandlerRequest(@NotNull Set<String> tokens, @NotNull String title,
                                         @NotNull String body)
{

}
