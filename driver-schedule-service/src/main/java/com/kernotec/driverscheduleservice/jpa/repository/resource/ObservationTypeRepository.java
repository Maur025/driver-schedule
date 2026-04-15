package com.kernotec.driverscheduleservice.jpa.repository.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.resource.ObservationType;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.observation.type.ObservationTypeLookupResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservationTypeRepository extends BaseRepository<ObservationType, UUID> {

    @Query("""
        SELECT ot.id as id, ot.name as name
        FROM ObservationType ot
        WHERE ot.deleted = false
        AND (:keyword IS NULL OR LOWER(ot.name) LIKE LOWER(CONCAT('%',:keyword,'%')))
        """)
    Page<ObservationTypeLookupResponse> findAllToLookup(@Param("keyword") String keyword,
        Pageable pageable);
}
