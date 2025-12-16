package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.command.cancel.reason.CancelReasonCreateCmd;
import com.kernotec.driverscheduleservice.jpa.entity.CancelReason;
import com.kernotec.driverscheduleservice.jpa.service.CancelReasonService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.CancelReasonSpec;
import com.kernotec.driverscheduleservice.rest.command.ProcessCancelReasonCreateRequestCmd;
import com.kernotec.driverscheduleservice.rest.dto.request.CancelReasonRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.CancelReasonResponse;
import com.kernotec.driverscheduleservice.rest.mapper.cancel.reason.CancelReasonResponseMapper;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = CancelReasonSpec.TAG_NAME, description = CancelReasonSpec.TAG_DESCRIPTION)
@RequestMapping(path = CancelReasonSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class CancelReasonController {

    private final CancelReasonService cancelReasonService;
    private final CancelReasonResponseMapper cancelReasonResponseMapper;
    private final CancelReasonCreateCmd cancelReasonCreateCmd;
    private final ProcessCancelReasonCreateRequestCmd processCancelReasonCreateRequestCmd;

    @Operation(summary = "find all cancel reasons")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<CancelReasonResponse> findAll(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<CancelReason> cancelReasonPage = cancelReasonService.findAll(pageable);

        return PageResponse.<CancelReasonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(cancelReasonResponseMapper.toResponse(cancelReasonPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(cancelReasonPage.getTotalElements())
                .pages(cancelReasonPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find cancel reasons without pagination")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<CancelReasonResponse> findAllWithoutPagination() {
        List<CancelReason> cancelReasonList = cancelReasonService.findAll();

        return PageResponse.<CancelReasonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(cancelReasonResponseMapper.toResponse(cancelReasonList))
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{cancelReasonId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<CancelReasonResponse> findById(@PathVariable() UUID cancelReasonId)
    {
        CancelReason cancelReason = cancelReasonService.findByIdThrow(cancelReasonId);

        return SingleResponse.<CancelReasonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(cancelReasonResponseMapper.toResponse(cancelReason))
            .build();
    }

    @Operation(summary = "save cancel reason")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResponse<CancelReasonResponse> saveCancelReason(
        @RequestBody CancelReasonRequest request)
    {

        UUID cancelReasonId = processCancelReasonCreateRequestCmd.withRequest(
                ProcessCancelReasonCreateRequestCmd.Request.builder()
                    .cancelReasonRequest(request)
                    .build())
            .execute();

        return SingleResponse.<CancelReasonResponse>builder()
            .code(HttpStatus.CREATED.value())
            .data(cancelReasonResponseMapper.toResponse(cancelReasonId))
            .build();
    }
}
