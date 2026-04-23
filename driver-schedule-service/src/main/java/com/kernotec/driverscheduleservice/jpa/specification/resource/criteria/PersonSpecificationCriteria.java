package com.kernotec.driverscheduleservice.jpa.specification.resource.criteria;

import com.kernotec.driverscheduleservice.jpa.enums.resource.PersonTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonSpecificationCriteria {

    private PersonTypeEnum personType;
}
