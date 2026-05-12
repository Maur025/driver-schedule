package com.kernotec.driverschedule.notification.push;

import com.kernotec.core.exception.custom.base.DefaultApiException;
import com.kernotec.driverschedule.notification.jpa.enums.CampaignRecipientEnum;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class NotificationFlowFactory {

    private final Map<CampaignRecipientEnum, NotificationFlowStrategy> strategies;

    public NotificationFlowFactory(List<NotificationFlowStrategy> notificationFlowStrategies) {
        this.strategies = notificationFlowStrategies.stream()
            .collect(Collectors.toMap(
                NotificationFlowStrategy::getFlowType, strategy -> strategy,
                (current, replacement) -> current
            ));
    }

    public NotificationFlowStrategy getStrategy(CampaignRecipientEnum flowType) {
        return Optional.ofNullable(strategies.get(flowType))
            .orElseThrow(
                () -> new DefaultApiException("No strategy found for flowType: " + flowType));
    }
}
