package com.kernotec.driverschedule.person.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonCsvImportDto {

    private String name;
    private String lastName;
    private String document;
    private String phone;
    private String username;
    private String personType;
}
