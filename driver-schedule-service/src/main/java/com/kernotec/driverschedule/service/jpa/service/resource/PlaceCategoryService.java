package com.kernotec.driverschedule.service.jpa.service.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.jpa.entity.resource.PlaceCategory;
import com.kernotec.driverschedule.service.jpa.repository.resource.PlaceCategoryRepository;
import com.kernotec.driverschedule.service.rest.dto.resource.response.place.category.PlaceCategoryLookupResponse;
import com.kernotec.driverschedule.common.util.CommonUtil;
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
