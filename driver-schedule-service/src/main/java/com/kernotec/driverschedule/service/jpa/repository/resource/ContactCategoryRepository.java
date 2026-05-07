package com.kernotec.driverschedule.service.jpa.repository.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.jpa.entity.resource.ContactCategory;
import com.kernotec.driverschedule.service.rest.dto.resource.response.contact.category.ContactCategoryLookupResponse;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactCategoryRepository extends BaseRepository<ContactCategory, UUID> {

    @Query("""
         SELECT cc.id as id, cc.name as name
         FROM ContactCategory cc
         WHERE cc.deleted = false
         AND (:keyword IS NULL OR LOWER(cc.name) LIKE LOWER(CONCAT('%',:keyword,'%')))
        """)
    Page<ContactCategoryLookupResponse> findAllToLookup(@Param("keyword") String keyword,
        Pageable pageable);

    Optional<ContactCategory> findByCode(String code);
}
