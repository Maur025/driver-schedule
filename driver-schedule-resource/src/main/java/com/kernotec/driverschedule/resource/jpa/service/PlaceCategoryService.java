package com.kernotec.driverschedule.resource.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.common.util.CommonUtil;
import com.kernotec.driverschedule.resource.jpa.entity.PlaceCategory;
import com.kernotec.driverschedule.resource.jpa.repository.PlaceCategoryRepository;
import com.kernotec.driverschedule.resource.rest.dto.response.PlaceCategoryLookupResponse;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PlaceCategoryService extends BaseServiceImpl<PlaceCategory, UUID> {

    private final PlaceCategoryRepository repository;

    @Override
    protected String resourceName() {
        return "Place Category";
    }

    @Override
    protected BaseRepository<PlaceCategory, UUID> repository() {
        return repository;
    }

    public Page<PlaceCategoryLookupResponse> findAllToLookup(String keyword, Pageable pageable) {
        String keywordStr = CommonUtil.getSafeString(keyword);
        return repository.findAllToLookup(keywordStr, pageable);
    }
}
