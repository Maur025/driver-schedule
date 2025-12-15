package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.Person;
import com.kernotec.driverscheduleservice.jpa.repository.PersonRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PersonService extends BaseServiceImpl<Person, UUID> {

    private final PersonRepository repository;

    @Override
    protected String resourceName() {
        return "Person";
    }

    @Override
    protected BaseRepository<Person, UUID> repository() {
        return repository;
    }
}
