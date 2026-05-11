package com.kernotec.driverschedule.service.resource.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.common.response.LookupResponse;
import com.kernotec.driverschedule.service.resource.jpa.entity.PlaceCategory;
import com.kernotec.driverschedule.service.resource.jpa.service.PlaceCategoryService;
import com.kernotec.driverschedule.service.resource.rest.ResourceApiSpec.PlaceCategorySpec;
import com.kernotec.driverschedule.service.resource.rest.dto.response.PlaceCategoryLookupResponse;
import com.kernotec.driverschedule.service.resource.rest.dto.response.PlaceCategoryResponse;
import com.kernotec.driverschedule.service.resource.rest.mapper.response.PlaceCategoryResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = PlaceCategorySpec.TAG_NAME, description = PlaceCategorySpec.TAG_DESCRIPTION)
@RequestMapping(path = PlaceCategorySpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class PlaceCategoryController {

    private final PlaceCategoryService placeCategoryService;
    private final PlaceCategoryResponseMapper placeCategoryResponseMapper;

    @Operation(summary = "find all place categories")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<PlaceCategoryResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<PlaceCategory> placeCategoryPage = placeCategoryService.findAll(pageable);

        return PageResponse.<PlaceCategoryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(placeCategoryResponseMapper.toResponse(placeCategoryPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(placeCategoryPage.getTotalElements())
                .pages(placeCategoryPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find place category by id")
    @GetMapping("{placeCategoryId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<PlaceCategoryResponse> findById(
        @PathVariable("placeCategoryId") UUID placeCategoryId)
    {
        PlaceCategory placeCategory = placeCategoryService.findByIdThrow(placeCategoryId);

        return SingleResponse.<PlaceCategoryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(placeCategoryResponseMapper.toResponse(placeCategory))
            .build();
    }

    @Operation(summary = "find all to lookup")
    @GetMapping("lookup")
    @ResponseStatus(HttpStatus.OK)
    public LookupResponse<List<PlaceCategoryLookupResponse>> findAllToLookup(
        @RequestParam(name = "keyword", required = false) String keyword)
    {
        Pageable pageable = PageableUtil.of(0, 500, "name", false);
        Page<PlaceCategoryLookupResponse> placeCategoryResponsePage = placeCategoryService.findAllToLookup(
            keyword, pageable);

        return LookupResponse.<List<PlaceCategoryLookupResponse>>builder()
            .code(HttpStatus.OK.value())
            .data(placeCategoryResponsePage.getContent())
            .build();
    }
}
