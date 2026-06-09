package com.kernotec.driverschedule.service.scheduling.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripAssignment;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TripAssignmentRepository extends BaseRepository<TripAssignment, UUID> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        DELETE FROM TripAssignment ta
        WHERE ta.scheduleTransportationId = :scheduleTransportationId
        """)
    void deleteAllByScheduleTransportationId(@Param("scheduleTransportationId") UUID scheduleTransportationId);
}
