package com.kernotec.driverschedule.person.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ContactCreateRequest extends BaseRequest {

    @NotNull
    private UUID labelTypeId;

    @NotNull
    @Size(max = 512)
    private String value;

    private UUID contactCategoryId;
    // Only update
    private UUID personId;
}
