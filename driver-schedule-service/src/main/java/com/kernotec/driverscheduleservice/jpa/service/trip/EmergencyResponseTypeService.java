package com.kernotec.driverscheduleservice.jpa.service.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.trip.EmergencyResponseType;
import com.kernotec.driverscheduleservice.jpa.repository.trip.EmergencyResponseTypeRepository;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.emergency.response.type.EmergencyResponseTypeLookupResponse;
import com.kernotec.driverscheduleservice.util.CommonUtil;
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
