package com.kernotec.driverschedule.service.rest.dto.resource.request.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.driverschedule.service.rest.dto.resource.request.contact.ContactCreateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class PersonCreateRequest extends BaseRequest {

    @NotNull
    private String name;
    @NotNull
    private String lastName;
    @NotNull
    private String document;
    @NotNull
    private Set<UUID> personTypeIds;
    @NotNull
    private String username;

    private List<@Valid ContactCreateRequest> contacts;
}
