package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.Contact;
import com.kernotec.driverscheduleservice.jpa.repository.ContactRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
