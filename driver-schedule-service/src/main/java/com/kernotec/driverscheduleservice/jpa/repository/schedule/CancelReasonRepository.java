package com.kernotec.driverscheduleservice.jpa.repository.schedule;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.CancelReason;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface CancelReasonRepository extends BaseRepository<CancelReason, UUID> {

}
