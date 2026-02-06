package com.kernotec.driverscheduleservice.rest.mapper.response.place.category;

import com.kernotec.driverscheduleservice.jpa.entity.PlaceCategory;
import com.kernotec.driverscheduleservice.rest.dto.response.place.category.PlaceCategoryResponse;
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
