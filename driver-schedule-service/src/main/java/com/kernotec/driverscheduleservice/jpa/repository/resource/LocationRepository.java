package com.kernotec.driverscheduleservice.jpa.repository.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Location;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends BaseRepository<Location, UUID> {

}
