package com.kernotec.driverschedule.service.jpa.service.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.jpa.entity.resource.ReasonType;
import com.kernotec.driverschedule.service.jpa.repository.resource.ReasonTypeRepository;
import com.kernotec.driverschedule.service.rest.dto.resource.response.reason.type.ReasonTypeLookupResponse;
import com.kernotec.driverschedule.service.util.CommonUtil;
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
