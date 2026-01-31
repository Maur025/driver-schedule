package com.kernotec.driverscheduleservice.rest.dto.response.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverscheduleservice.audit.user.dto.response.AuditEntityUserResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.person.type.PersonTypeResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.contact.ContactResponse;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class PersonResponse extends AuditEntityUserResponse {

    private String name;
    private String lastName;
    private String document;
    private UUID userId;

    private Set<PersonTypeResponse> personTypes;
    private Set<ContactResponse> contacts;
}
