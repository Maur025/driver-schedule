package com.kernotec.driverschedule.service.resource.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.resource.jpa.entity.Reason;
import com.kernotec.driverschedule.service.resource.rest.dto.response.ReasonLookupResponse;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReasonRepository extends BaseRepository<Reason, UUID> {

/*    @Query("""
        SELECT DISTINCT r
        FROM Reason r
        JOIN r.transportationRejectedRequests tr
        WHERE tr.id = :transportationRequestId
        """)
    Set<Reason> findRejectByTransportationRequestId(
        @Param("transportationRequestId") UUID transportationRequestId);*/

    @Query("""
        SELECT r.id as id, r.value as value, r.code as code
        FROM Reason r
        LEFT JOIN ReasonType rt ON r.reasonTypeId = rt.id
        WHERE r.deleted = false
        AND (:keyword IS NULL OR LOWER(r.value) LIKE LOWER(CONCAT('%',:keyword,'%')))
        AND ( CAST(:reasonTypeId AS uuid) IS NULL OR r.reasonTypeId = :reasonTypeId)
        AND ( :reasonType IS NULL OR rt.code = :reasonType )
        GROUP BY r.id, r.value
        """)
    Page<ReasonLookupResponse> findAllToLookup(@Param("keyword") String keyword,
        @Param("reasonTypeId") UUID reasonTypeId, @Param("reasonType") String reasonType,
        Pageable pageable);

    List<Reason> findByIdIn(Collection<UUID> ids);
}
