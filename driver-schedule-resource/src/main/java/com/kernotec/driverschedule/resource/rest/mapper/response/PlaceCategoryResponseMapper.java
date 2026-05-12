package com.kernotec.driverschedule.resource.rest.mapper.response;

import com.kernotec.driverschedule.resource.jpa.entity.PlaceCategory;
import com.kernotec.driverschedule.resource.rest.dto.response.PlaceCategoryResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface PlaceCategoryResponseMapper {

    PlaceCategoryResponse toResponse(PlaceCategory placeCategory);

    PlaceCategoryResponse toResponse(UUID id);

    List<PlaceCategoryResponse> toResponse(List<PlaceCategory> placeCategoryList);

    Set<PlaceCategoryResponse> toResponse(Set<PlaceCategory> placeCategorySet);
}
