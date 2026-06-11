package com.kernotec.driverschedule.service.scheduling.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.EmergencyRejectReason;
import com.kernotec.driverschedule.service.scheduling.jpa.service.EmergencyRejectReasonService;
import com.kernotec.driverschedule.service.scheduling.rest.TripApiSpec.EmergencyRejectReasonSpec;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip.EmergencyRejectReasonResponse;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip.EmergencyRejectReasonResponseMapper;
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

@Tag(name = EmergencyRejectReasonSpec.TAG_NAME,
     description = EmergencyRejectReasonSpec.TAG_DESCRIPTION)
@RequestMapping(path = EmergencyRejectReasonSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class EmergencyRejectReasonController {

    private final EmergencyRejectReasonService emergencyRejectReasonService;
    private final EmergencyRejectReasonResponseMapper emergencyRejectReasonResponseMapper;

    @Operation(summary = "find all")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<EmergencyRejectReasonResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdBy") String sortBy,
        @RequestParam(name = "descending", defaultValue = "false") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<EmergencyRejectReason> emergencyRejectReasonPage = emergencyRejectReasonService.findAll(
            pageable);

        return PageResponse.<EmergencyRejectReasonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(emergencyRejectReasonResponseMapper.toResponse(
                emergencyRejectReasonPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(emergencyRejectReasonPage.getTotalElements())
                .pages(emergencyRejectReasonPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{emergencyRejectReasonId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<EmergencyRejectReasonResponse> findById(
        @PathVariable("emergencyRejectReasonId") UUID emergencyRejectReasonId)
    {
        EmergencyRejectReason emergencyRejectReason = emergencyRejectReasonService.findByIdThrow(
            emergencyRejectReasonId);

        return SingleResponse.<EmergencyRejectReasonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(emergencyRejectReasonResponseMapper.toResponse(emergencyRejectReason))
            .build();
    }
}
