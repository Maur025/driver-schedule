package com.kernotec.driverschedule.service.jpa.service.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.jpa.entity.resource.LabelType;
import com.kernotec.driverschedule.service.jpa.repository.resource.LabelTypeRepository;
import com.kernotec.driverschedule.service.rest.dto.resource.response.label.type.LabelTypeLookupResponse;
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
