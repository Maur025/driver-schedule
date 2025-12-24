package com.kernotec.driverscheduleservice.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportationState;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleTransportationStateRepository extends
    BaseRepository<ScheduleTransportationState, UUID>
{

    Optional<ScheduleTransportationState> findByCode(String code);
}
