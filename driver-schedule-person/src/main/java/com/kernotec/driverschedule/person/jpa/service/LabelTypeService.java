package com.kernotec.driverschedule.person.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.person.jpa.entity.LabelType;
import com.kernotec.driverschedule.person.jpa.repository.LabelTypeRepository;
import com.kernotec.driverschedule.person.rest.dto.response.LabelTypeLookupResponse;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LabelTypeService extends BaseServiceImpl<LabelType, UUID> {

    private final LabelTypeRepository repository;

    @Override
    protected String resourceName() {
        return "Label Type";
    }

    @Override
    protected BaseRepository<LabelType, UUID> repository() {
        return repository;
    }

    public Page<LabelTypeLookupResponse> findAllToLookup(Pageable pageable) {
        return repository.findAllToLookup(pageable);
    }
}
