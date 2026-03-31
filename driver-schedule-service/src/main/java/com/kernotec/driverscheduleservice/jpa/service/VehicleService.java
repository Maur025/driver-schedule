package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.Vehicle;
import com.kernotec.driverscheduleservice.jpa.repository.VehicleRepository;
import com.kernotec.driverscheduleservice.rest.dto.response.vehicle.VehicleLookupResponse;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class VehicleService extends BaseServiceImpl<Vehicle, UUID> {

    private final VehicleRepository repository;

    @Override
    protected String resourceName() {
        return "Vehicle";
    }

    @Override
    protected BaseRepository<Vehicle, UUID> repository() {
        return repository;
    }

    public Page<VehicleLookupResponse> findAllToLookup(@Param("keyword") String keyword,
        Pageable pageable)
    {
        String keywordStr = keyword == null || keyword.isBlank() ? null : keyword;

        return repository.findAllToLookup(keywordStr, pageable);
    }
}
