package com.kernotec.driverschedule.service.jpa.repository.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.jpa.entity.trip.TripState;
import com.kernotec.driverschedule.service.rest.dto.trip.response.trip.state.TripStateLookupResponse;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TripStateRepository extends BaseRepository<TripState, UUID> {

    @Query("""
        SELECT ts.id as id, ts.name as name
        FROM TripState ts
        WHERE ts.deleted = false
        AND (:keyword IS NULL OR LOWER(ts.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
        """)
    Page<TripStateLookupResponse> findAllToLookup(@Param("keyword") String keyword,
        Pageable pageable);

    Optional<TripState> findByCode(String code);
}
