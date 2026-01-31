package com.kernotec.driverscheduleservice.rest.mapper.contact;

import com.kernotec.driverscheduleservice.jpa.entity.Contact;
import com.kernotec.driverscheduleservice.rest.dto.response.contact.ContactResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ContactResponseFlatMapper {

    @Mapping(target = "person", ignore = true)
    ContactResponse toResponse(Contact contact);

    ContactResponse toResponse(UUID id);

    List<ContactResponse> toResponse(List<Contact> contactList);

    Set<ContactResponse> toResponse(Set<Contact> contactSet);
}
