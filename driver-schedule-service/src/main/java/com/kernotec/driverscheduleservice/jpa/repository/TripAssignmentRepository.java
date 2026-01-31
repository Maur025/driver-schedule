package com.kernotec.driverscheduleservice.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.TripAssignment;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TripAssignmentRepository extends BaseRepository<TripAssignment, UUID> {

}
