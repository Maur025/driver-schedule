package com.kernotec.driverscheduleservice.rest.dto.resource.request.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class LocationCreateRequest extends BaseRequest {

    @NotNull
    private List<Double> coordinates;

    @NotBlank
    @NotNull
    private String name;

    private String description;
    private String icon;
    private String color;

    @NotNull
    private UUID placeCategoryId;
}
