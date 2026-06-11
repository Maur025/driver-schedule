package com.kernotec.driverschedule.service.scheduling.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.RejectReason;
import com.kernotec.driverschedule.service.scheduling.jpa.service.RejectReasonService;
import com.kernotec.driverschedule.service.scheduling.rest.RequestApiSpec.RejectReasonSpec;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.RejectReasonResponse;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.RejectReasonResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = RejectReasonSpec.TAG_NAME, description = RejectReasonSpec.TAG_DESCRIPTION)
@RequestMapping(path = RejectReasonSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class RejectReasonController {

    private final RejectReasonService rejectReasonService;
    private final RejectReasonResponseMapper rejectReasonResponseMapper;

    @Operation(summary = "find all reject reasons")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<RejectReasonResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<RejectReason> rejectReasonPage = rejectReasonService.findAll(pageable);

        return PageResponse.<RejectReasonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(rejectReasonResponseMapper.toResponse(rejectReasonPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(rejectReasonPage.getTotalElements())
                .pages(rejectReasonPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find reject reason by id")
    @GetMapping("{rejectReasonId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<RejectReasonResponse> findById(
        @PathVariable("rejectReasonId") UUID rejectReasonId)
    {
        RejectReason rejectReason = rejectReasonService.findByIdThrow(rejectReasonId);

        return SingleResponse.<RejectReasonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(rejectReasonResponseMapper.toResponse(rejectReason))
            .build();
    }
}
