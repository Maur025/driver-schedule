package com.kernotec.driverschedule.service.request.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.request.jpa.entity.RejectReason;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface RejectReasonRepository extends BaseRepository<RejectReason, UUID> {

}
