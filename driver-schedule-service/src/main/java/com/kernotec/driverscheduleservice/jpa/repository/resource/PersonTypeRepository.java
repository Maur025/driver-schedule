package com.kernotec.driverscheduleservice.jpa.repository.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.resource.PersonType;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonTypeRepository extends BaseRepository<PersonType, UUID> {

}
