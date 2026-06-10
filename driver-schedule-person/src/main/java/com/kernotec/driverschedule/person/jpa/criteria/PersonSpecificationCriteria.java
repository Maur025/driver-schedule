package com.kernotec.driverschedule.person.jpa.criteria;

import com.kernotec.driverschedule.person.jpa.enums.PersonTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonSpecificationCriteria {

    private PersonTypeEnum personType;
    private Boolean deleted;
}
