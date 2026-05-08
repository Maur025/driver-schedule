package com.kernotec.driverschedule.notification.notification.service;

import com.kernotec.driverschedule.notification.jpa.enums.CampaignRecipientEnum;
import com.kernotec.driverschedule.notification.jpa.service.NotificationConfigurationService;
import com.kernotec.driverschedule.notification.handler.NotificationHandler;
import com.kernotec.driverschedule.notification.notification.dto.NotificationSendRequest;
import com.kernotec.driverschedule.person.jpa.dto.PersonDto;
import com.kernotec.driverschedule.person.jpa.enums.PersonTypeEnum;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationAllDriversStrategy extends NotificationFlowStrategy {

    private final PersonService personService;

    public NotificationAllDriversStrategy(
        NotificationConfigurationService notificationConfigurationService,
        NotificationHandler notificationHandler, PersonService personService)
    {
        super(notificationConfigurationService, notificationHandler);
        this.personService = personService;
    }

    @Override
    protected CampaignRecipientEnum getFlowType() {
        return CampaignRecipientEnum.ALL_DRIVERS;
    }

    @Override
    protected Set<UUID> getPersonIds(NotificationSendRequest request) {
        List<PersonDto> personDtoList = personService.findAllDtoByPersonTypesToNotification(
            Set.of(PersonTypeEnum.DRIVER));

        if (personDtoList.isEmpty()) {
            log.debug("No person with the type DRIVER wa found");
            return null;
        }

        return personDtoList.stream()
            .map(PersonDto::getId)
            .collect(Collectors.toSet());
    }
}
