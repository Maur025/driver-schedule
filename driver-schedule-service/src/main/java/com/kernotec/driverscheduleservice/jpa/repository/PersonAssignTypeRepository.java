package com.kernotec.driverscheduleservice.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.PersonAssignType;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonAssignTypeRepository extends BaseRepository<PersonAssignType, UUID> {

    void deleteAllByPersonId(UUID personId);
}
