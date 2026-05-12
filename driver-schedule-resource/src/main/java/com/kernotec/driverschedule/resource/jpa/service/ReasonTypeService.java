package com.kernotec.driverschedule.resource.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.common.util.CommonUtil;
import com.kernotec.driverschedule.resource.jpa.entity.ReasonType;
import com.kernotec.driverschedule.resource.jpa.repository.ReasonTypeRepository;
import com.kernotec.driverschedule.resource.rest.dto.response.ReasonTypeLookupResponse;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReasonTypeService extends BaseServiceImpl<ReasonType, UUID> {

    private final ReasonTypeRepository repository;

    @Override
    protected String resourceName() {
        return "Reason Type";
    }

    @Override
    protected BaseRepository<ReasonType, UUID> repository() {
        return repository;
    }

    public Page<ReasonTypeLookupResponse> findAllToLookup(String keyword, Pageable pageable)
    {
        String keywordStr = CommonUtil.getSafeString(keyword);
        return repository.findAllToLookup(keywordStr, pageable);
    }
}
