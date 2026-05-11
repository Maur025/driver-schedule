package com.kernotec.driverschedule.service.resource.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.resource.jpa.entity.Vehicle;
import com.kernotec.driverschedule.service.resource.rest.dto.response.VehicleLookupResponse;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends BaseRepository<Vehicle, UUID> {

    @Query("""
        SELECT v.id as id, v.vehicleNumber as vehicleNumber
        FROM Vehicle v
        WHERE v.deleted = false
        AND (:keyword IS NULL OR LOWER(v.vehicleNumber) LIKE LOWER(CONCAT('%',:keyword,'%')))
        """)
    Page<VehicleLookupResponse> findAllToLookup(@Param("keyword") String keyword,
        Pageable pageable);

    List<Vehicle> findByIdInAndDeleted(Collection<UUID> ids, boolean deleted);
}
