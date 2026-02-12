package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.PersonAssignType;
import com.kernotec.driverscheduleservice.jpa.repository.PersonAssignTypeRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class PersonAssignTypeService extends BaseServiceImpl<PersonAssignType, UUID> {

    private final PersonAssignTypeRepository repository;

    @Override
    protected String resourceName() {
        return "Person Assign Type";
    }

    @Override
    protected BaseRepository<PersonAssignType, UUID> repository() {
        return repository;
    }

    @Transactional
    public void deleteAllByPersonId(UUID personId) {
        repository.deleteAllByPersonId(personId);
    }
}
