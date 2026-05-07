package com.kernotec.driverschedule.service.rest.controller.trip;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.service.jpa.entity.trip.EmergencyResponseType;
import com.kernotec.driverschedule.service.jpa.service.trip.EmergencyResponseTypeService;
import com.kernotec.driverschedule.service.rest.ApiSpec.EmergencyResponseTypeSpec;
import com.kernotec.driverschedule.service.rest.dto.common.response.LookupResponse;
import com.kernotec.driverschedule.service.rest.dto.trip.response.emergency.response.type.EmergencyResponseTypeLookupResponse;
import com.kernotec.driverschedule.service.rest.dto.trip.response.emergency.response.type.EmergencyResponseTypeResponse;
import com.kernotec.driverschedule.service.rest.mapper.trip.response.emergency.response.type.EmergencyResponseTypeResponseMapper;
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

@Tag(name = EmergencyResponseTypeSpec.TAG_NAME,
     description = EmergencyResponseTypeSpec.TAG_DESCRIPTION)
@RequestMapping(path = EmergencyResponseTypeSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class EmergencyResponseTypeController {

    private final EmergencyResponseTypeService emergencyResponseTypeService;
    private final EmergencyResponseTypeResponseMapper emergencyResponseTypeResponseMapper;

    @Operation(summary = "find all emergency response types")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<EmergencyResponseTypeResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "false") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        Page<EmergencyResponseType> emergencyResponseTypePage = emergencyResponseTypeService.findAll(
            pageable);

        return PageResponse.<EmergencyResponseTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(emergencyResponseTypeResponseMapper.toResponse(
                emergencyResponseTypePage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(emergencyResponseTypePage.getTotalElements())
                .pages(emergencyResponseTypePage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{emergencyResponseTypeId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<EmergencyResponseTypeResponse> findById(
        @PathVariable("emergencyResponseTypeId") UUID emergencyResponseTypeId)
    {
        EmergencyResponseType emergencyResponseType = emergencyResponseTypeService.findByIdThrow(
            emergencyResponseTypeId);

        return SingleResponse.<EmergencyResponseTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(emergencyResponseTypeResponseMapper.toResponse(emergencyResponseType))
            .build();
    }

    @Operation(summary = "find all to lookup")
    @GetMapping("lookup")
    @ResponseStatus(HttpStatus.OK)
    public LookupResponse<List<EmergencyResponseTypeLookupResponse>> findAllToLookup(
        @RequestParam(name = "keyword", required = false) String keyword)
    {
        Pageable pageable = PageableUtil.of(0, 25, "name", false);
        Page<EmergencyResponseTypeLookupResponse> emergencyResponseTypeLookupResponsePage = emergencyResponseTypeService.findAllToLookup(
            keyword, pageable);

        return LookupResponse.<List<EmergencyResponseTypeLookupResponse>>builder()
            .code(HttpStatus.OK.value())
            .data(emergencyResponseTypeLookupResponsePage.getContent())
            .build();
    }
}
