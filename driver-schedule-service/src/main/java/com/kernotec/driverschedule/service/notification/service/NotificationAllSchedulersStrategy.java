package com.kernotec.driverschedule.service.notification.service;

import com.kernotec.driverschedule.service.jpa.dto.resource.PersonDto;
import com.kernotec.driverschedule.service.jpa.enums.notification.CampaignRecipientEnum;
import com.kernotec.driverschedule.service.jpa.enums.resource.PersonTypeEnum;
import com.kernotec.driverschedule.service.jpa.service.notification.NotificationConfigurationService;
import com.kernotec.driverschedule.service.jpa.service.resource.PersonService;
import com.kernotec.driverschedule.service.notification.NotificationHandler;
import com.kernotec.driverschedule.service.notification.dto.NotificationSendRequest;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationAllSchedulersStrategy extends NotificationFlowStrategy {

    private final PersonService personService;

    public NotificationAllSchedulersStrategy(
        NotificationConfigurationService notificationConfigurationService,
        NotificationHandler notificationHandler, PersonService personService)
    {
        super(notificationConfigurationService, notificationHandler);
        this.personService = personService;
    }

    @Override
    public CampaignRecipientEnum getFlowType() {
        return CampaignRecipientEnum.ALL_SCHEDULERS;
    }

    @Override
    protected Set<UUID> getPersonIds(NotificationSendRequest request) {
        List<PersonDto> personDtoList = personService.findAllDtoByPersonTypesToNotification(
            Set.of(PersonTypeEnum.SCHEDULER));

        if (personDtoList.isEmpty()) {
            log.debug("No persons with the type SCHEDULER was found");
            return null;
        }

        return personDtoList.stream()
            .map(PersonDto::getId)
            .collect(Collectors.toSet());
    }
}
