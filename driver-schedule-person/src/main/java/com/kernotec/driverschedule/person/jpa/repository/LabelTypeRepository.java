package com.kernotec.driverschedule.person.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.person.jpa.entity.LabelType;
import com.kernotec.driverschedule.person.rest.dto.response.LabelTypeLookupResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelTypeRepository extends BaseRepository<LabelType, UUID> {

    @Query("""
        SELECT lt.id as id, lt.name as name
        FROM LabelType lt
        WHERE lt.deleted = false
        """)
    Page<LabelTypeLookupResponse> findAllToLookup(Pageable pageable);
}
