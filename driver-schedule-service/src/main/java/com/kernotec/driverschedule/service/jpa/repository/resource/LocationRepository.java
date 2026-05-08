package com.kernotec.driverschedule.service.jpa.repository.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.jpa.entity.resource.Location;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends BaseRepository<Location, UUID> {

}
