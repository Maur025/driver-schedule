package com.kernotec.driverscheduleservice.jpa.service.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Observation;
import com.kernotec.driverscheduleservice.jpa.enums.resource.ObservationTypeCodeEnum;
import com.kernotec.driverscheduleservice.jpa.repository.resource.ObservationRepository;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.observation.ObservationLookupResponse;
import com.kernotec.driverscheduleservice.util.CommonUtil;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ObservationService extends BaseServiceImpl<Observation, UUID> {

    private final ObservationRepository repository;

    @Override
    protected String resourceName() {
        return "Observation";
    }

    @Override
    protected BaseRepository<Observation, UUID> repository() {
        return repository;
    }

    public Page<ObservationLookupResponse> findAllToLookup(String keyword,
        ObservationTypeCodeEnum observationType, Pageable pageable)
    {
        String keywordStr = CommonUtil.getSafeString(keyword);
        String observationStr = observationType != null ? observationType.toString() : null;

        return repository.findAllToLookup(keywordStr, observationStr, pageable);
    }
}
