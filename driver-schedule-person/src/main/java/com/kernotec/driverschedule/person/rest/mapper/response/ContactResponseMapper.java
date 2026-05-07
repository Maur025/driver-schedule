package com.kernotec.driverschedule.person.rest.mapper.response;

import com.kernotec.driverschedule.person.jpa.entity.Contact;
import com.kernotec.driverschedule.person.rest.dto.response.ContactResponse;
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
