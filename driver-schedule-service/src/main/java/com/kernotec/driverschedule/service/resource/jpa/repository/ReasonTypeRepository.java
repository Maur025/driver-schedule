package com.kernotec.driverschedule.service.resource.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.resource.jpa.entity.ReasonType;
import com.kernotec.driverschedule.service.resource.rest.dto.response.ReasonTypeLookupResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReasonTypeRepository extends BaseRepository<ReasonType, UUID> {

    @Query("""
        SELECT rt.id as id, rt.name as name
        FROM ReasonType rt
        WHERE rt.deleted = false
        AND (:keyword IS NULL OR LOWER(rt.name) LIKE LOWER(CONCAT('%',:keyword,'%')))
        """)
    Page<ReasonTypeLookupResponse> findAllToLookup(@Param("keyword") String keyword,
        Pageable pageable);
}
