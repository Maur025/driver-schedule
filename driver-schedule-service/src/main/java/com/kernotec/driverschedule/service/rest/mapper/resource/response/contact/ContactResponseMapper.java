package com.kernotec.driverschedule.service.rest.mapper.resource.response.contact;

import com.kernotec.driverschedule.service.jpa.entity.resource.Contact;
import com.kernotec.driverschedule.service.rest.dto.resource.response.contact.ContactResponse;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.person.PersonResponseFlatMapper;
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
