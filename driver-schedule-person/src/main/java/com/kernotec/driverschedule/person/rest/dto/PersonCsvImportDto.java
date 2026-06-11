package com.kernotec.driverschedule.person.rest.dto;

import com.kernotec.driverschedule.person.jpa.enums.PersonTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonCsvImportDto {

    private String name;
    private String lastName;
    private String document;
    private String phoneWhatsapp;
    private String phoneWork;
    private String username;
    private PersonTypeEnum personType;
}
