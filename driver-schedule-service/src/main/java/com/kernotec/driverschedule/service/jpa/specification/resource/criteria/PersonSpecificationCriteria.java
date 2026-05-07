package com.kernotec.driverschedule.service.jpa.specification.resource.criteria;

import com.kernotec.driverschedule.service.jpa.enums.resource.PersonTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonSpecificationCriteria {

    private PersonTypeEnum personType;
}
