package com.kernotec.driverscheduleservice.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.Trip;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends BaseRepository<Trip, UUID> {

    Optional<Trip> findByTripAssignmentIdAndDeleted(UUID tripAssignmentId, Boolean deleted);
}
