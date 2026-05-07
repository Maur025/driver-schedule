package com.kernotec.driverschedule.service.rest.mapper.resource.request.contact;

import com.kernotec.driverschedule.service.jpa.entity.resource.Contact;
import com.kernotec.driverschedule.service.rest.dto.resource.request.contact.ContactCreateRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ContactEntityMapper {

    @Mapping(target = "contactCategoryId", source = "contactCategoryId")
    @Mapping(target = "personId", source = "personId")
    Contact toEntity(ContactCreateRequest contactCreateRequest, UUID contactCategoryId,
        UUID personId);

    default List<Contact> toEntity(List<ContactCreateRequest> requestList, UUID contactCategoryId,
        UUID personId)
    {
        if (requestList == null) {
            return null;
        }

        List<Contact> list = new ArrayList<>(requestList.size());

        for (ContactCreateRequest contactCreateRequest : requestList) {
            list.add(toEntity(contactCreateRequest, contactCategoryId, personId));
        }

        return list;
    }
}
