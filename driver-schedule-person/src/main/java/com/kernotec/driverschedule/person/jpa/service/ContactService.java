package com.kernotec.driverschedule.person.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.person.jpa.entity.Contact;
import com.kernotec.driverschedule.person.jpa.repository.ContactRepository;
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
