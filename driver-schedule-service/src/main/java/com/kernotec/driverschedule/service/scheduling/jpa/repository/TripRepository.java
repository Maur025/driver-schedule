package com.kernotec.driverschedule.service.scheduling.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.Trip;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends BaseRepository<Trip, UUID> {

    Optional<Trip> findByTripAssignmentIdAndDeleted(UUID tripAssignmentId, Boolean deleted);

    Page<Trip> findByTripAssignmentIdInAndDeleted(Set<UUID> tripAssignmentIds, Boolean deleted,
        Pageable pageable);

    List<Trip> findByIdInAndDeleted(Set<UUID> tripIds, Boolean deleted);
}
