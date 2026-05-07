package com.kernotec.driverschedule.service.rest.mapper.resource.response.place.category;

import com.kernotec.driverschedule.service.jpa.entity.resource.PlaceCategory;
import com.kernotec.driverschedule.service.rest.dto.resource.response.place.category.PlaceCategoryResponse;
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
