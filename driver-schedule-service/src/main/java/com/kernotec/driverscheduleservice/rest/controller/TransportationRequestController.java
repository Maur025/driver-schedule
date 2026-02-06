package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.config.KernotecApiDefinition;
import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.service.TransportationRequestService;
import com.kernotec.driverscheduleservice.report.jpa.enums.ReportDispositionEnum;
import com.kernotec.driverscheduleservice.report.rest.command.pdf.PdfExportCmd;
import com.kernotec.driverscheduleservice.rest.ApiSpec.TransportationRequestSpec;
import com.kernotec.driverscheduleservice.rest.command.transportation.request.ProcessTransportationRequestCancelledCmd;
import com.kernotec.driverscheduleservice.rest.command.transportation.request.ProcessTransportationRequestCreateRequestCmd;
import com.kernotec.driverscheduleservice.rest.command.transportation.request.ProcessTransportationRequestRejectedCmd;
import com.kernotec.driverscheduleservice.rest.command.transportation.request.VoucherTransportationRequestPdfExportCmd;
import com.kernotec.driverscheduleservice.rest.dto.request.cancel.request.reason.CancelRequestReasonRequest;
import com.kernotec.driverscheduleservice.rest.dto.request.reject.reason.RejectReasonRequest;
import com.kernotec.driverscheduleservice.rest.dto.request.transportation.request.TransportationRequestCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.request.transportation.request.TransportationRequestFilterRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.transportation.request.TransportationRequestResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.transportation.request.TransportationRequestResponseMapper;
import com.kernotec.driverscheduleservice.util.AppRoleUtil.IsRoleApplicantOrScheduler;
import com.kernotec.driverscheduleservice.util.AppRoleUtil.IsRoleSchedulerOrAdmin;
import com.kernotec.driverscheduleservice.util.VoucherJasperUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = TransportationRequestSpec.TAG_NAME,
     description = TransportationRequestSpec.TAG_DESCRIPTION)
@RequestMapping(path = TransportationRequestSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class TransportationRequestController {

    private final TransportationRequestService transportationRequestService;

    private final TransportationRequestResponseMapper transportationRequestResponseMapper;

    private final ProcessTransportationRequestCreateRequestCmd processTransportationRequestCreateRequestCmd;
    private final ProcessTransportationRequestRejectedCmd processTransportationRequestRejectedCmd;
    private final ProcessTransportationRequestCancelledCmd processTransportationRequestCancelledCmd;
    private final PdfExportCmd pdfExportCmd;
    private final VoucherTransportationRequestPdfExportCmd voucherTransportationRequestPdfExportCmd;

    private final KernotecApiDefinition kernotecApiDefinition;
    private final VoucherJasperUtil voucherJasperUtil;

    @Operation(summary = "find all transportation requests")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TransportationRequestResponse> findAll(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        Page<TransportationRequest> transportationRequestPage = transportationRequestService.findAll(
            pageable);

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

    @Operation(summary = "find all transportation requests with filters")
    @PostMapping("filter")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TransportationRequestResponse> findAllFilter(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") Boolean descending,
        @RequestBody TransportationRequestFilterRequest filterRequest,
        Authentication authentication)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        Page<TransportationRequest> transportationRequestPage = transportationRequestService.findAllWithFilters(
            filterRequest, authentication, pageable);

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

    @Operation(summary = "find by id")
    @GetMapping("{transportationRequestId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<TransportationRequestResponse> findById(
        @PathVariable UUID transportationRequestId)
    {
        TransportationRequest transportationRequest = transportationRequestService.findByIdThrow(
            transportationRequestId);

        return SingleResponse.<TransportationRequestResponse>builder()
            .code(HttpStatus.OK.value())
            .data(transportationRequestResponseMapper.toResponse(transportationRequest))
            .build();
    }

    @Operation(summary = "save transportation request")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @IsRoleApplicantOrScheduler
    public SingleResponse<TransportationRequestResponse> save(
        @RequestBody TransportationRequestCreateRequest request, Authentication authentication)
    {
        TransportationRequest transportationRequest = processTransportationRequestCreateRequestCmd.withRequest(
                ProcessTransportationRequestCreateRequestCmd.Request.builder()
                    .transportationRequestCreateRequest(request)
                    .authentication(authentication)
                    .build())
            .execute();

        String uri = voucherJasperUtil.getVoucherUrl(
            "/transportation-requests/{id}/voucher", transportationRequest.getId());

        return SingleResponse.<TransportationRequestResponse>builder()
            .code(HttpStatus.CREATED.value())
            .data(transportationRequestResponseMapper.toResponse(
                transportationRequest.getId(), transportationRequest.getCorrelative(),
                transportationRequest.getCode(), uri
            ))
            .build();
    }

    @Operation(summary = "reject transportation request")
    @PostMapping("{transportationRequestId}/rejected")
    @ResponseStatus(HttpStatus.OK)
    @IsRoleSchedulerOrAdmin
    public SingleResponse<TransportationRequestResponse> rejectedRequest(
        @PathVariable UUID transportationRequestId, @RequestBody RejectReasonRequest request)
    {
        processTransportationRequestRejectedCmd.withRequest(
                ProcessTransportationRequestRejectedCmd.Request.builder()
                    .transportationRequestId(transportationRequestId)
                    .rejectReasonRequest(request)
                    .build())
            .execute();

        return SingleResponse.<TransportationRequestResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Transportation request rejected successfully")
            .build();
    }

    @Operation(summary = "cancel transportation request")
    @PostMapping("{transportationRequestId}/cancelled")
    @ResponseStatus(HttpStatus.OK)
    @IsRoleApplicantOrScheduler
    public SingleResponse<TransportationRequestResponse> cancelledRequest(
        @PathVariable UUID transportationRequestId, @RequestBody CancelRequestReasonRequest request)
    {
        processTransportationRequestCancelledCmd.withRequest(
                ProcessTransportationRequestCancelledCmd.Request.builder()
                    .transportationRequestId(transportationRequestId)
                    .cancelRequestReasonRequest(request)
                    .build())
            .execute();

        return SingleResponse.<TransportationRequestResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Transportation request cancelled successfully")
            .build();
    }

    @Operation(summary = "transportation request export voucher")
    @GetMapping("{transportationRequestId}/voucher")
    @ResponseStatus(HttpStatus.OK)
    public void transportationRequestExportVoucher(@PathVariable UUID transportationRequestId,
        @RequestParam(defaultValue = "America/La_Paz") String zoneId,
        @RequestParam(defaultValue = "inline") ReportDispositionEnum disposition,
        HttpServletResponse response)
    {
        pdfExportCmd.withRequest(PdfExportCmd.Request.builder()
                .response(response)
                .disposition(disposition)
                .fileName("request-voucher")
                .callbackGetReportBytes(() -> voucherTransportationRequestPdfExportCmd.withRequest(
                        VoucherTransportationRequestPdfExportCmd.Request.builder()
                            .transportationRequestId(transportationRequestId)
                            .zoneId(zoneId)
                            .build())
                    .execute())
                .build())
            .execute();

    }
}
