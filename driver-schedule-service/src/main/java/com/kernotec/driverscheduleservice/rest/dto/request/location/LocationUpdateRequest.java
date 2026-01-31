package com.kernotec.driverscheduleservice.rest.dto.request.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class LocationUpdateRequest extends BaseRequest {

    private List<Double> coordinates;
    private String name;
    private String description;
    private String icon;
    private String color;
    private UUID placeCategoryId;
}
