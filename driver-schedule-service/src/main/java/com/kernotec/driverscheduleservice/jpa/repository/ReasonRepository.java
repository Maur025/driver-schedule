package com.kernotec.driverscheduleservice.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.Reason;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReasonRepository extends BaseRepository<Reason, UUID> {

    @Query("""
        SELECT DISTINCT r
        FROM Reason r
        JOIN r.transportationRejectedRequests tr
        WHERE tr.id = :transportationRequestId
        """)
    Set<Reason> findRejectByTransportationRequestId(
        @Param("transportationRequestId") UUID transportationRequestId);
}
