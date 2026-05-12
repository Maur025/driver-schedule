package com.kernotec.driverschedule.resource.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.resource.jpa.entity.Location;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends BaseRepository<Location, UUID> {

}
