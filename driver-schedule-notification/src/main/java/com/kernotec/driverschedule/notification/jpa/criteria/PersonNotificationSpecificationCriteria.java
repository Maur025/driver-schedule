package com.kernotec.driverschedule.notification.jpa.criteria;

import com.kernotec.driverschedule.notification.jpa.enums.PersonNotificationState;
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
