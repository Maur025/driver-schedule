package com.kernotec.driverschedule.service.resource.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.resource.jpa.entity.VehicleType;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleTypeRepository extends BaseRepository<VehicleType, UUID> {

}
