package com.kernotec.driverscheduleservice.jpa.repository.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Vehicle;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.vehicle.VehicleLookupResponse;
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
}
