package com.kernotec.driverscheduleservice.jpa.repository.schedule;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.TripAssignmentState;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TripAssignmentStateRepository extends BaseRepository<TripAssignmentState, UUID> {

    Optional<TripAssignmentState> findByCode(String code);
}
