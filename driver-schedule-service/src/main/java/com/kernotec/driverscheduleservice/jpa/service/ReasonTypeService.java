package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.ReasonType;
import com.kernotec.driverscheduleservice.jpa.repository.ReasonTypeRepository;
import com.kernotec.driverscheduleservice.rest.dto.response.reason.type.ReasonTypeLookupResponse;
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
        return repository.findAllToLookup(keyword, pageable);
    }
}
