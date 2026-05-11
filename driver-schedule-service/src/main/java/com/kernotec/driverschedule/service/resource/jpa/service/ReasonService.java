package com.kernotec.driverschedule.service.resource.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.resource.jpa.entity.Reason;
import com.kernotec.driverschedule.service.resource.jpa.enums.ReasonTypeEnum;
import com.kernotec.driverschedule.service.resource.jpa.repository.ReasonRepository;
import com.kernotec.driverschedule.service.resource.rest.dto.response.ReasonLookupResponse;
import com.kernotec.driverschedule.common.util.CommonUtil;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReasonService extends BaseServiceImpl<Reason, UUID> {

    private final ReasonRepository repository;

    @Override
    protected String resourceName() {
        return "Reason";
    }

    @Override
    protected BaseRepository<Reason, UUID> repository() {
        return repository;
    }

    /*public Set<Reason> findRejectByTransportationRequestId(UUID transportationRequestId) {
        return repository.findRejectByTransportationRequestId(transportationRequestId);
    }*/

    public Page<ReasonLookupResponse> findAllToLookup(String keyword, UUID reasonTypeId,
        ReasonTypeEnum reasonType, Pageable pageable)
    {
        String keywordStr = CommonUtil.getSafeString(keyword);
        String reasonTypeStr = reasonType != null ? String.valueOf(reasonType) : null;

        return repository.findAllToLookup(keywordStr, reasonTypeId, reasonTypeStr, pageable);
    }

    public List<Reason> findByIdIn(Collection<UUID> ids) {
        return repository.findByIdIn(ids);
    }
}
