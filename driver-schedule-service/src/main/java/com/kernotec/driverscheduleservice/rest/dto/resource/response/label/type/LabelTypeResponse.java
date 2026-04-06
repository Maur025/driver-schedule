package com.kernotec.driverscheduleservice.rest.dto.resource.response.label.type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.contact.category.ContactCategoryResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class LabelTypeResponse extends EntityResponse {

    private String name;
    private String code;

    private UUID contactCategoryId;
    private ContactCategoryResponse contactCategory;
}
