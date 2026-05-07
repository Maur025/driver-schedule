package com.kernotec.driverschedule.service.jpa.repository.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.jpa.entity.resource.Observation;
import com.kernotec.driverschedule.service.rest.dto.resource.response.observation.ObservationLookupResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservationRepository extends BaseRepository<Observation, UUID> {

    @Query("""
        SELECT o.id as id, o.name as name
        FROM Observation o
        INNER JOIN ObservationType ot ON ot.id = o.observationTypeId
        WHERE o.deleted = true
        AND (:observationType IS NULL OR ot.code = :observationType)
        AND (:keyword IS NULL OR LOWER(o.name) LIKE LOWER(CONCAT('%',:keyword, '%')))
        GROUP BY o.id, o.name
        """)
    Page<ObservationLookupResponse> findAllToLookup(@Param("keyword") String keyword,
        @Param("observationType") String observationType, Pageable pageable);
}
