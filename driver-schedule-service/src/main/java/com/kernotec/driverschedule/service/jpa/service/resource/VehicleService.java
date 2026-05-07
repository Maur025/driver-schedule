package com.kernotec.driverschedule.service.jpa.service.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.jpa.entity.resource.Vehicle;
import com.kernotec.driverschedule.service.jpa.repository.resource.VehicleRepository;
import com.kernotec.driverschedule.service.rest.dto.resource.response.vehicle.VehicleLookupResponse;
import com.kernotec.driverschedule.common.util.CommonUtil;
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
