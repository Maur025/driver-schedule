package com.kernotec.driverschedule.person.rest.mapper.response;

import com.kernotec.driverschedule.person.jpa.entity.ContactCategory;
import com.kernotec.driverschedule.person.rest.dto.response.ContactCategoryResponse;
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
