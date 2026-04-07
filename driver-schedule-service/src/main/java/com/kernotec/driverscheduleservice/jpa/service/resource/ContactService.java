package com.kernotec.driverscheduleservice.jpa.service.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Contact;
import com.kernotec.driverscheduleservice.jpa.repository.resource.ContactRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class ContactService extends BaseServiceImpl<Contact, UUID> {

    private final ContactRepository repository;

    @Override
    protected String resourceName() {
        return "Contact";
    }

    @Override
    protected BaseRepository<Contact, UUID> repository() {
        return repository;
    }

    @Transactional
    public void deleteAllByPersonId(UUID personId) {
        repository.deleteAllByPersonId(personId);
    }
}
