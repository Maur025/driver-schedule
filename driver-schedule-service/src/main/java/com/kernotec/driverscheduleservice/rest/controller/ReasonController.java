package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.jpa.entity.Reason;
import com.kernotec.driverscheduleservice.jpa.enums.ReasonTypeEnum;
import com.kernotec.driverscheduleservice.jpa.service.ReasonService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.ReasonSpec;
import com.kernotec.driverscheduleservice.rest.dto.response.LookupResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.reason.ReasonLookupResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.reason.ReasonResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.reason.ReasonResponseMapper;
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

@Tag(name = ReasonSpec.TAG_NAME, description = ReasonSpec.TAG_DESCRIPTION)
@RequestMapping(path = ReasonSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class ReasonController {

    private final ReasonService reasonService;
    private final ReasonResponseMapper reasonResponseMapper;

    @Operation(summary = "find all reasons")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ReasonResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<Reason> reasonPage = reasonService.findAll(pageable);

        return PageResponse.<ReasonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(reasonResponseMapper.toResponse(reasonPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(reasonPage.getTotalElements())
                .pages(reasonPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{reasonId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ReasonResponse> findById(@PathVariable() UUID reasonId)
    {
        Reason reason = reasonService.findByIdThrow(reasonId);

        return SingleResponse.<ReasonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(reasonResponseMapper.toResponse(reason))
            .build();
    }

    @Operation(summary = "find all to lookup")
    @GetMapping("lookup")
    @ResponseStatus(HttpStatus.OK)
    public LookupResponse<List<ReasonLookupResponse>> findAllToLookup(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) UUID reasonTypeId,
        @RequestParam(required = false) ReasonTypeEnum reasonType)

    {
        Pageable pageable = PageableUtil.of(0, 500, "value", false);
        Page<ReasonLookupResponse> reasonLookupResponsePage = reasonService.findAllToLookup(
            keyword, reasonTypeId, reasonType, pageable);

        return LookupResponse.<List<ReasonLookupResponse>>builder()
            .code(HttpStatus.OK.value())
            .data(reasonLookupResponsePage.getContent())
            .build();
    }
}
