package com.kernotec.driverscheduleservice.rest.mapper.resource.response.contact;

import com.kernotec.driverscheduleservice.jpa.entity.resource.Contact;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.contact.ContactResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.person.PersonResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {PersonResponseFlatMapper.class})
public interface ContactResponseMapper {

    ContactResponse toResponse(Contact contact);

    ContactResponse toResponse(UUID id);

    List<ContactResponse> toResponse(List<Contact> contactList);

    Set<ContactResponse> toResponse(Set<Contact> contactSet);
}
