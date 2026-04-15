package com.kernotec.driverscheduleservice.rest.controller.resource;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.jpa.entity.resource.ObservationType;
import com.kernotec.driverscheduleservice.jpa.service.resource.ObservationTypeService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.ObservationTypeSpec;
import com.kernotec.driverscheduleservice.rest.dto.common.response.LookupResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.observation.type.ObservationTypeLookupResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.observation.type.ObservationTypeResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.observation.type.ObservationTypeResponseMapper;
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

@Tag(name = ObservationTypeSpec.TAG_NAME, description = ObservationTypeSpec.TAG_DESCRIPTION)
@RequestMapping(path = ObservationTypeSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ObservationTypeController {

    private final ObservationTypeService observationTypeService;
    private final ObservationTypeResponseMapper observationTypeResponseMapper;

    @Operation(summary = "find all observation types")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ObservationTypeResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "false") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        Page<ObservationType> observationTypePage = observationTypeService.findAll(pageable);

        return PageResponse.<ObservationTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(observationTypeResponseMapper.toResponse(observationTypePage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(observationTypePage.getTotalElements())
                .pages(observationTypePage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{observationTypeId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ObservationTypeResponse> findById(
        @PathVariable("observationTypeId") UUID observationTypeId)
    {
        ObservationType observationType = observationTypeService.findByIdThrow(observationTypeId);

        return SingleResponse.<ObservationTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(observationTypeResponseMapper.toResponse(observationType))
            .build();
    }

    @Operation(summary = "find all to lookup")
    @GetMapping("lookup")
    @ResponseStatus(HttpStatus.OK)
    public LookupResponse<List<ObservationTypeLookupResponse>> findAllToLookup(
        @RequestParam(name = "keyword", required = false) String keyword)
    {
        Pageable pageable = PageableUtil.of(0, 25, "name", false);
        Page<ObservationTypeLookupResponse> observationTypeLookupResponsePage = observationTypeService.findAllToLookup(
            keyword, pageable);

        return LookupResponse.<List<ObservationTypeLookupResponse>>builder()
            .code(HttpStatus.OK.value())
            .data(observationTypeLookupResponsePage.getContent())
            .build();
    }
}
