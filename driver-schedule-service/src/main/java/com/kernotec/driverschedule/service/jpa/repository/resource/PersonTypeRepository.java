package com.kernotec.driverschedule.service.jpa.repository.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.jpa.entity.resource.PersonType;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonTypeRepository extends BaseRepository<PersonType, UUID> {

}
