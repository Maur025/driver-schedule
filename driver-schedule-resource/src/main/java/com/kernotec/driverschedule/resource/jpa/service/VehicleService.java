package com.kernotec.driverschedule.resource.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.common.util.CommonUtil;
import com.kernotec.driverschedule.resource.jpa.entity.Vehicle;
import com.kernotec.driverschedule.resource.jpa.repository.VehicleRepository;
import com.kernotec.driverschedule.resource.rest.dto.response.VehicleLookupResponse;
import java.util.Collection;
import java.util.List;
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
        String keywordStr = CommonUtil.getSafeString(keyword);

        return repository.findAllToLookup(keywordStr, pageable);
    }

    public List<Vehicle> findByIdInAndDeleted(Collection<UUID> ids, boolean deleted) {
        return repository.findByIdInAndDeleted(ids, deleted);
    }

    public List<Vehicle> findCanNotUsed(Collection<UUID> vehicleIds) {
        return findByIdInAndDeleted(vehicleIds, true);
    }
}
