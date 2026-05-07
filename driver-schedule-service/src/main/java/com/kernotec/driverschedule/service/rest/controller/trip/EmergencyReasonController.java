package com.kernotec.driverschedule.service.rest.controller.trip;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.common.rest.ApiSpec.EmergencyReasonSpec;
import com.kernotec.driverschedule.service.jpa.entity.trip.EmergencyReason;
import com.kernotec.driverschedule.service.jpa.service.trip.EmergencyReasonService;
import com.kernotec.driverschedule.service.rest.dto.trip.response.emergency.reason.EmergencyReasonResponse;
import com.kernotec.driverschedule.service.rest.mapper.trip.response.emergency.reason.EmergencyReasonResponseMapper;
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

@Tag(name = EmergencyReasonSpec.TAG_NAME, description = EmergencyReasonSpec.TAG_DESCRIPTION)
@RequestMapping(path = EmergencyReasonSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class EmergencyReasonController {

    private final EmergencyReasonService emergencyReasonService;
    private final EmergencyReasonResponseMapper emergencyReasonResponseMapper;

    @Operation(summary = "find all")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<EmergencyReasonResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdBy") String sortBy,
        @RequestParam(name = "descending", defaultValue = "false") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<EmergencyReason> emergencyReasonPage = emergencyReasonService.findAll(pageable);

        return PageResponse.<EmergencyReasonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(emergencyReasonResponseMapper.toResponse(emergencyReasonPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(emergencyReasonPage.getTotalElements())
                .pages(emergencyReasonPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{emergencyReasonId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<EmergencyReasonResponse> findById(
        @PathVariable("emergencyReasonId") UUID emergencyReasonId)
    {
        EmergencyReason emergencyReason = emergencyReasonService.findByIdThrow(emergencyReasonId);

        return SingleResponse.<EmergencyReasonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(emergencyReasonResponseMapper.toResponse(emergencyReason))
            .build();
    }
}
