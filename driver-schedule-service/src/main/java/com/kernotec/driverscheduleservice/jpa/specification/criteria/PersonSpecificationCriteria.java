package com.kernotec.driverscheduleservice.jpa.specification.criteria;

import com.kernotec.driverscheduleservice.jpa.enums.PersonTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonSpecificationCriteria {

    private PersonTypeEnum personType;
}
