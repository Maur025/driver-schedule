package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.VehicleType;
import com.kernotec.driverscheduleservice.jpa.repository.VehicleTypeRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class VehicleTypeService extends BaseServiceImpl<VehicleType, UUID> {

    private final VehicleTypeRepository repository;

    @Override
    protected String resourceName() {
        return "Vehicle Type";
    }

    @Override
    protected BaseRepository<VehicleType, UUID> repository() {
        return repository;
    }
}
