package com.kernotec.driverschedule.service.rest.controller.resource;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.service.jpa.entity.resource.LabelType;
import com.kernotec.driverschedule.service.jpa.service.resource.LabelTypeService;
import com.kernotec.driverschedule.service.rest.ApiSpec.LabelTypeSpec;
import com.kernotec.driverschedule.service.rest.dto.common.response.LookupResponse;
import com.kernotec.driverschedule.service.rest.dto.resource.response.label.type.LabelTypeLookupResponse;
import com.kernotec.driverschedule.service.rest.dto.resource.response.label.type.LabelTypeResponse;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.label.type.LabelTypeResponseMapper;
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

@Tag(name = LabelTypeSpec.TAG_NAME, description = LabelTypeSpec.TAG_DESCRIPTION)
@RequestMapping(path = LabelTypeSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class LabelTypeController {

    private final LabelTypeService labelTypeService;
    private final LabelTypeResponseMapper labelTypeResponseMapper;

    @Operation(summary = "find label types ")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<LabelTypeResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<LabelType> labelTypePage = labelTypeService.findAll(pageable);

        return PageResponse.<LabelTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(labelTypeResponseMapper.toResponse(labelTypePage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(labelTypePage.getTotalElements())
                .pages(labelTypePage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find label type by id")
    @GetMapping("{labelTypeId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<LabelTypeResponse> findById(@PathVariable("labelTypeId") UUID labelTypeId)
    {
        LabelType labelType = labelTypeService.findByIdThrow(labelTypeId);

        return SingleResponse.<LabelTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(labelTypeResponseMapper.toResponse(labelType))
            .build();
    }

    @Operation(summary = "find all to lookup")
    @GetMapping("lookup")
    @ResponseStatus(HttpStatus.OK)
    public LookupResponse<List<LabelTypeLookupResponse>> findAllToLookup() {
        Pageable pageable = PageableUtil.of(0, 500, "name", false);
        Page<LabelTypeLookupResponse> labelTypePage = labelTypeService.findAllToLookup(pageable);

        return LookupResponse.<List<LabelTypeLookupResponse>>builder()
            .code(HttpStatus.OK.value())
            .data(labelTypePage.getContent())
            .build();
    }
}
