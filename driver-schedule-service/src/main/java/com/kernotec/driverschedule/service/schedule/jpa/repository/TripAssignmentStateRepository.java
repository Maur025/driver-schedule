package com.kernotec.driverschedule.service.schedule.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.schedule.jpa.entity.TripAssignmentState;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TripAssignmentStateRepository extends BaseRepository<TripAssignmentState, UUID> {

    Optional<TripAssignmentState> findByCode(String code);
}
