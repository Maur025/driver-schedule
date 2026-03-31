package com.kernotec.driverscheduleservice.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.Trip;
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
}
