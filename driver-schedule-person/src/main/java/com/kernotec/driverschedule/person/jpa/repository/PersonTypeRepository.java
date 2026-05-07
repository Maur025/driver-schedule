package com.kernotec.driverschedule.person.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.person.jpa.entity.PersonType;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonTypeRepository extends BaseRepository<PersonType, UUID> {

}
