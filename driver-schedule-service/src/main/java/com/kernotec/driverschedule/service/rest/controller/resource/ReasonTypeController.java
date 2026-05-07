package com.kernotec.driverschedule.service.rest.controller.resource;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.service.jpa.entity.resource.ReasonType;
import com.kernotec.driverschedule.service.jpa.service.resource.ReasonTypeService;
import com.kernotec.driverschedule.service.rest.ApiSpec.ReasonTypeSpec;
import com.kernotec.driverschedule.service.rest.dto.common.response.LookupResponse;
import com.kernotec.driverschedule.service.rest.dto.resource.response.reason.type.ReasonTypeLookupResponse;
import com.kernotec.driverschedule.service.rest.dto.resource.response.reason.type.ReasonTypeResponse;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.reason.type.ReasonTypeResponseMapper;
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

@Tag(name = ReasonTypeSpec.TAG_NAME, description = ReasonTypeSpec.TAG_DESCRIPTION)
@RequestMapping(path = ReasonTypeSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ReasonTypeController {

    private final ReasonTypeService reasonTypeService;
    private final ReasonTypeResponseMapper reasonTypeResponseMapper;

    @Operation(summary = "find all reason types")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ReasonTypeResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<ReasonType> reasonTypePage = reasonTypeService.findAll(pageable);

        return PageResponse.<ReasonTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(reasonTypeResponseMapper.toResponse(reasonTypePage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(reasonTypePage.getTotalElements())
                .pages(reasonTypePage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find reason type by id")
    @GetMapping("{reasonTypeId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ReasonTypeResponse> findById(
        @PathVariable("reasonTypeId") UUID reasonTypeId)
    {
        ReasonType reasonType = reasonTypeService.findByIdThrow(reasonTypeId);

        return SingleResponse.<ReasonTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(reasonTypeResponseMapper.toResponse(reasonType))
            .build();
    }

    @Operation(summary = "find all to lookup")
    @GetMapping("lookup")
    @ResponseStatus(HttpStatus.OK)
    public LookupResponse<List<ReasonTypeLookupResponse>> findAllToLookup(
        @RequestParam(name = "keyword", required = false) String keyword)
    {
        Pageable pageable = PageableUtil.of(0, 500, "name", false);
        Page<ReasonTypeLookupResponse> placeCategoryResponsePage = reasonTypeService.findAllToLookup(
            keyword, pageable);

        return LookupResponse.<List<ReasonTypeLookupResponse>>builder()
            .code(HttpStatus.OK.value())
            .data(placeCategoryResponsePage.getContent())
            .build();
    }
}
