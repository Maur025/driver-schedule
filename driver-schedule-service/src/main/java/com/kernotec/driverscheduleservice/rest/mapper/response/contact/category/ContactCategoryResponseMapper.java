package com.kernotec.driverscheduleservice.rest.mapper.response.contact.category;

import com.kernotec.driverscheduleservice.jpa.entity.resource.ContactCategory;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.contact.category.ContactCategoryResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface ContactCategoryResponseMapper {

    ContactCategoryResponse toResponse(ContactCategory contactCategory);

    ContactCategoryResponse toResponse(UUID id);

    List<ContactCategoryResponse> toResponse(List<ContactCategory> contactCategoryList);

    Set<ContactCategoryResponse> toResponse(Set<ContactCategory> contactCategorySet);
}
