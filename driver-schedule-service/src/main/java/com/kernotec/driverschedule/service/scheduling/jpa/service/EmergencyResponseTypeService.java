package com.kernotec.driverschedule.service.scheduling.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.EmergencyResponseType;
import com.kernotec.driverschedule.service.scheduling.jpa.repository.EmergencyResponseTypeRepository;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip.EmergencyResponseTypeLookupResponse;
import com.kernotec.driverschedule.common.util.CommonUtil;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmergencyResponseTypeService extends BaseServiceImpl<EmergencyResponseType, UUID> {

    private final EmergencyResponseTypeRepository repository;

    @Override
    protected String resourceName() {
        return "Emergency Response Type";
    }

    @Override
    protected BaseRepository<EmergencyResponseType, UUID> repository() {
        return repository;
    }

    public Page<EmergencyResponseTypeLookupResponse> findAllToLookup(String keyword,
        Pageable pageable)
    {
        String keywordStr = CommonUtil.getSafeString(keyword);

        return repository.findAllToLookup(keywordStr, pageable);
    }
}
