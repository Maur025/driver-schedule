package com.kernotec.driverschedule.service.rest.controller.trip;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.common.rest.ApiSpec.EmergencyResponseSpec;
import com.kernotec.driverschedule.service.jpa.entity.trip.EmergencyResponse;
import com.kernotec.driverschedule.service.jpa.service.trip.EmergencyResponseService;
import com.kernotec.driverschedule.service.rest.dto.trip.response.emergency.response.EmergencyResponseResponse;
import com.kernotec.driverschedule.service.rest.mapper.trip.response.emergency.response.EmergencyResponseResponseMapper;
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

@Tag(name = EmergencyResponseSpec.TAG_NAME, description = EmergencyResponseSpec.TAG_DESCRIPTION)
@RequestMapping(path = EmergencyResponseSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class EmergencyResponseController {

    private final EmergencyResponseService emergencyResponseService;
    private final EmergencyResponseResponseMapper emergencyResponseResponseMapper;

    @Operation(summary = "find all emergency responses")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<EmergencyResponseResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "false") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        Page<EmergencyResponse> emergencyResponsePage = emergencyResponseService.findAll(pageable);

        return PageResponse.<EmergencyResponseResponse>builder()
            .code(HttpStatus.OK.value())
            .data(emergencyResponseResponseMapper.toResponse(emergencyResponsePage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(emergencyResponsePage.getTotalElements())
                .pages(emergencyResponsePage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{emergencyResponseId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<EmergencyResponseResponse> findById(
        @PathVariable("emergencyResponseId") UUID emergencyResponseId)
    {
        EmergencyResponse emergencyResponse = emergencyResponseService.findByIdThrow(
            emergencyResponseId);

        return SingleResponse.<EmergencyResponseResponse>builder()
            .code(HttpStatus.OK.value())
            .data(emergencyResponseResponseMapper.toResponse(emergencyResponse))
            .build();
    }
}
