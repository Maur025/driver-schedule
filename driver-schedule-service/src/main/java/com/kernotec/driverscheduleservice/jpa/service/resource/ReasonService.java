package com.kernotec.driverscheduleservice.jpa.service.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Reason;
import com.kernotec.driverscheduleservice.jpa.enums.resource.ReasonTypeEnum;
import com.kernotec.driverscheduleservice.jpa.repository.resource.ReasonRepository;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.reason.ReasonLookupResponse;
import com.kernotec.driverscheduleservice.util.CommonUtil;
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
}
