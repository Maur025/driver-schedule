package com.kernotec.driverschedule.service.jpa.specification.notification.criteria;

import com.kernotec.driverschedule.service.jpa.enums.notification.PersonNotificationState;
import java.util.Collection;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonNotificationSpecificationCriteria {

    private UUID personId;
    private Collection<PersonNotificationState> states;
    private Boolean deleted;
}
