package com.kernotec.driverschedule.service.rest.controller.resource;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.common.rest.ApiSpec.ObservationSpec;
import com.kernotec.driverschedule.service.jpa.entity.resource.Observation;
import com.kernotec.driverschedule.service.jpa.enums.resource.ObservationTypeCodeEnum;
import com.kernotec.driverschedule.service.jpa.service.resource.ObservationService;
import com.kernotec.driverschedule.common.response.LookupResponse;
import com.kernotec.driverschedule.service.rest.dto.resource.response.observation.ObservationLookupResponse;
import com.kernotec.driverschedule.service.rest.dto.resource.response.observation.ObservationResponse;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.observation.ObservationResponseMapper;
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

@Tag(name = ObservationSpec.TAG_NAME, description = ObservationSpec.TAG_DESCRIPTION)
@RequestMapping(path = ObservationSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ObservationController {

    private final ObservationService observationService;
    private final ObservationResponseMapper observationResponseMapper;

    @Operation(summary = "find all observations")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ObservationResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "false") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        Page<Observation> observationPage = observationService.findAll(pageable);

        return PageResponse.<ObservationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(observationResponseMapper.toResponse(observationPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(observationPage.getTotalElements())
                .pages(observationPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{observationId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ObservationResponse> findById(
        @PathVariable("observationId") UUID observationId)
    {
        Observation observation = observationService.findByIdThrow(observationId);

        return SingleResponse.<ObservationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(observationResponseMapper.toResponse(observation))
            .build();
    }

    @Operation(summary = "find all to lookup")
    @GetMapping("lookup")
    @ResponseStatus(HttpStatus.OK)
    public LookupResponse<List<ObservationLookupResponse>> findAllToLookup(
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "observationType",
                      required = false) ObservationTypeCodeEnum observationType)
    {
        Pageable pageable = PageableUtil.of(0, 25, "name", false);
        Page<ObservationLookupResponse> observationLookupResponsePage = observationService.findAllToLookup(
            keyword, observationType, pageable);

        return LookupResponse.<List<ObservationLookupResponse>>builder()
            .code(HttpStatus.OK.value())
            .data(observationLookupResponsePage.getContent())
            .build();
    }
}
