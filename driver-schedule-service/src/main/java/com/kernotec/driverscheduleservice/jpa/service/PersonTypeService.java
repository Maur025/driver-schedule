package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.PersonType;
import com.kernotec.driverscheduleservice.jpa.repository.PersonTypeRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PersonTypeService extends BaseServiceImpl<PersonType, UUID> {

    private final PersonTypeRepository repository;

    @Override
    protected String resourceName() {
        return "Person Type";
    }

    @Override
    protected BaseRepository<PersonType, UUID> repository() {
        return repository;
    }
}
