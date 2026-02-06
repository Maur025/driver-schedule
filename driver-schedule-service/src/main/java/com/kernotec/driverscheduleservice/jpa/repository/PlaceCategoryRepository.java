package com.kernotec.driverscheduleservice.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.PlaceCategory;
import com.kernotec.driverscheduleservice.rest.dto.response.place.category.PlaceCategoryLookupResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceCategoryRepository extends BaseRepository<PlaceCategory, UUID> {

    @Query("""
        SELECT pc.id as id, pc.name as name
        FROM PlaceCategory pc
        WHERE pc.deleted = false
        AND (:keyword IS NULL OR LOWER(pc.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
        """)
    Page<PlaceCategoryLookupResponse> findAllToLookup(@Param("keyword") String keyword,
        Pageable pageable);
}
