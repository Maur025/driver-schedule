package com.kernotec.driverschedule.service.rest.dto.resource.response.contact;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverschedule.service.rest.dto.resource.response.label.type.LabelTypeResponse;
import com.kernotec.driverschedule.service.rest.dto.resource.response.person.PersonResponse;
import com.kernotec.driverschedule.service.rest.dto.resource.response.contact.category.ContactCategoryResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ContactResponse extends EntityResponse {

    private String value;

    private UUID labelTypeId;
    private LabelTypeResponse labelType;

    private UUID contactCategoryId;
    private ContactCategoryResponse contactCategory;

    private UUID personId;
    private PersonResponse person;
}
