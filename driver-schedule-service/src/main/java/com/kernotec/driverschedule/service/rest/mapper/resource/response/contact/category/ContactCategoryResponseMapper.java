package com.kernotec.driverschedule.service.rest.mapper.resource.response.contact.category;

import com.kernotec.driverschedule.service.jpa.entity.resource.ContactCategory;
import com.kernotec.driverschedule.service.rest.dto.resource.response.contact.category.ContactCategoryResponse;
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
