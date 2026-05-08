package com.kernotec.driverschedule.person.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@JsonInclude(Include.NON_NULL)
public class PersonUpdateRequest extends BaseRequest {

    private String name;
    private String lastName;
    private String document;
    private Set<UUID> personTypeIds;

    private List<@Valid ContactCreateRequest> contacts;
}
