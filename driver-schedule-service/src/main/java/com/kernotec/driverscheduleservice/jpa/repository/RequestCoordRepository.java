package com.kernotec.driverscheduleservice.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.RequestCoord;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestCoordRepository extends BaseRepository<RequestCoord, UUID> {

}
