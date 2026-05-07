package com.kernotec.driverschedule.service.jpa.service.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.jpa.entity.resource.ObservationType;
import com.kernotec.driverschedule.service.jpa.repository.resource.ObservationTypeRepository;
import com.kernotec.driverschedule.service.rest.dto.resource.response.observation.type.ObservationTypeLookupResponse;
import com.kernotec.driverschedule.service.util.CommonUtil;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ObservationTypeService extends BaseServiceImpl<ObservationType, UUID> {

    private final ObservationTypeRepository repository;

    @Override
    protected String resourceName() {
        return "Observation Type";
    }

    @Override
    protected BaseRepository<ObservationType, UUID> repository() {
        return repository;
    }

    public Page<ObservationTypeLookupResponse> findAllToLookup(String keyword, Pageable pageable)
    {
        String keywordStr = CommonUtil.getSafeString(keyword);
        return repository.findAllToLookup(keywordStr, pageable);
    }
}
