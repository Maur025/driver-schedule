package com.kernotec.driverschedule.resource.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.resource.jpa.entity.VehicleType;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleTypeRepository extends BaseRepository<VehicleType, UUID> {

}
