package com.kernotec.driverscheduleservice.report.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.report.jpa.service.ReportTransportationRequestService;
import com.kernotec.driverscheduleservice.report.rest.dto.request.ReportTransportationRequestRequest;
import com.kernotec.driverscheduleservice.rest.ApiSpec.ReportSpec;
import com.kernotec.driverscheduleservice.rest.dto.response.TransportationRequestResponse;
import com.kernotec.driverscheduleservice.rest.mapper.transportation.request.TransportationRequestResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = ReportSpec.TAG_NAME, description = ReportSpec.TAG_DESCRIPTION)
@RequestMapping(path = ReportSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class ReportTransportationRequestController {

    private final ReportTransportationRequestService reportTransportationRequestService;
    private final TransportationRequestResponseMapper transportationRequestResponseMapper;

    @Operation(summary = "transportation request report preview with filters")
    @PostMapping("transportation-requests/report")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TransportationRequestResponse> findAllWithFilters(
        @RequestBody ReportTransportationRequestRequest request,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<TransportationRequest> transportationRequestPage = reportTransportationRequestService.findAllWithFilters(
            request, pageable);

        return PageResponse.<TransportationRequestResponse>builder()
            .code(HttpStatus.OK.value())
            .data(transportationRequestResponseMapper.toResponse(
                transportationRequestPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(transportationRequestPage.getTotalElements())
                .pages(transportationRequestPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "export transportation request report to excel with filters")
    @PostMapping("transportation-requests/report/excel")
    @ResponseStatus(HttpStatus.OK)
    public void exportTrasportationRequestReportToExcel(HttpServletResponse response,
        @RequestParam(required = false) String titleReport,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") Boolean descending, Authentication authentication)
    {
    }
}
