package com.kernotec.driverscheduleservice.request.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.request.jpa.entity.CancelRequestReason;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface CancelRequestReasonRepository extends BaseRepository<CancelRequestReason, UUID> {

}
