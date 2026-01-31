package com.kernotec.driverscheduleservice.rest.dto.response.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.place.category.PlaceCategoryResponse;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class LocationResponse extends EntityResponse {

    private Double latitude;
    private Double longitude;
    private List<Double> coords;

    private String name;
    private String description;
    private String icon;
    private String color;

    private UUID placeCategoryId;
    private PlaceCategoryResponse placeCategory;
}
