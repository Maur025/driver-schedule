package com.kernotec.driverschedule.service.resource.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.resource.jpa.entity.ObservationType;
import com.kernotec.driverschedule.service.resource.jpa.repository.ObservationTypeRepository;
import com.kernotec.driverschedule.service.resource.rest.dto.response.ObservationTypeLookupResponse;
import com.kernotec.driverschedule.common.util.CommonUtil;
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
