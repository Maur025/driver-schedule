package com.kernotec.driverscheduleservice.jpa.repository.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.trip.EmergencyResponseType;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.emergency.response.type.EmergencyResponseTypeLookupResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyResponseTypeRepository extends
    BaseRepository<EmergencyResponseType, UUID>
{

    @Query("""
        SELECT ert.id as id, ert.name as name, ert.code as code
        FROM EmergencyResponseType ert
        WHERE ert.deleted = false
        AND (:keyword IS NULL OR LOWER(ert.name) LIKE LOWER(CONCAT('%',:keyword,'%')))
        """)
    Page<EmergencyResponseTypeLookupResponse> findAllToLookup(@Param("keyword") String keyword,
        Pageable pageable);

}
