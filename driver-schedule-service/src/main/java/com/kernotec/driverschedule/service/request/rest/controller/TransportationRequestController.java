package com.kernotec.driverschedule.service.request.rest.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.service.common.annotation.transportation.request.CanCancelRequest;
import com.kernotec.driverschedule.service.common.annotation.transportation.request.CanCreateRequest;
import com.kernotec.driverschedule.service.common.annotation.transportation.request.CanReadRequest;
import com.kernotec.driverschedule.service.common.annotation.transportation.request.CanRejectRequest;
import com.kernotec.driverschedule.service.report.jpa.enums.ReportDispositionEnum;
import com.kernotec.driverschedule.service.report.rest.command.pdf.PdfExportCmd;
import com.kernotec.driverschedule.service.request.jpa.entity.TransportationRequest;
import com.kernotec.driverschedule.service.request.jpa.service.TransportationRequestService;
import com.kernotec.driverschedule.service.request.rest.ApiRequestSpec.TransportationRequestSpec;
import com.kernotec.driverschedule.service.request.rest.command.ProcessTransportationRequestCancelledCmd;
import com.kernotec.driverschedule.service.request.rest.command.ProcessTransportationRequestCreateRequestCmd;
import com.kernotec.driverschedule.service.request.rest.command.ProcessTransportationRequestRejectedCmd;
import com.kernotec.driverschedule.service.request.rest.command.VoucherTransportationRequestPdfExportCmd;
import com.kernotec.driverschedule.service.request.rest.dto.request.CancelRequestReasonRequest;
import com.kernotec.driverschedule.service.request.rest.dto.request.RejectReasonRequest;
import com.kernotec.driverschedule.service.request.rest.dto.request.TransportationRequestCreateRequest;
import com.kernotec.driverschedule.service.request.rest.dto.request.TransportationRequestFilterRequest;
import com.kernotec.driverschedule.service.request.rest.dto.response.TransportationRequestResponse;
import com.kernotec.driverschedule.service.request.rest.mapper.response.TransportationRequestResponseMapper;
import com.kernotec.driverschedule.common.response.SingleHateoasResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "find all transportation requests")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CanReadRequest
    public PageResponse<TransportationRequestResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending)
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
    @CanReadRequest
    public PageResponse<TransportationRequestResponse> findAllFilter(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending,
        @RequestBody TransportationRequestFilterRequest filterRequest)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<TransportationRequest> transportationRequestPage = transportationRequestService.findAllWithFilters(
            filterRequest, pageable);

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
    @CanReadRequest
    public SingleResponse<TransportationRequestResponse> findById(
        @PathVariable("transportationRequestId") UUID transportationRequestId)
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
    @CanCreateRequest
    public SingleHateoasResponse<TransportationRequestResponse> save(
        @RequestBody TransportationRequestCreateRequest request)
    {
        TransportationRequest transportationRequest = processTransportationRequestCreateRequestCmd.withRequest(
                ProcessTransportationRequestCreateRequestCmd.Request.builder()
                    .transportationRequestCreateRequest(request)
                    .build())
            .execute();

        return SingleHateoasResponse.<TransportationRequestResponse>builder()
            .code(HttpStatus.CREATED.value())
            .data(transportationRequestResponseMapper.toResponse(
                transportationRequest.getId(), transportationRequest.getCorrelative(),
                transportationRequest.getCode()
            ))
            .links(List.of(linkTo(
                methodOn(TransportationRequestController.class).transportationRequestExportVoucher(
                    transportationRequest.getId(), null, ReportDispositionEnum.inline)).withRel(
                    "voucher")
                .expand()))
            .build();
    }

    @Operation(summary = "reject transportation request")
    @PostMapping("{transportationRequestId}/rejected")
    @ResponseStatus(HttpStatus.OK)
    @CanRejectRequest
    public SingleHateoasResponse<TransportationRequestResponse> rejectedRequest(
        @PathVariable("transportationRequestId") UUID transportationRequestId,
        @RequestBody RejectReasonRequest request)
    {
        processTransportationRequestRejectedCmd.withRequest(
                ProcessTransportationRequestRejectedCmd.Request.builder()
                    .transportationRequestId(transportationRequestId)
                    .rejectReasonRequest(request)
                    .build())
            .execute();

        return SingleHateoasResponse.<TransportationRequestResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Transportation request rejected successfully")
            .links(List.of(linkTo(
                methodOn(TransportationRequestController.class).transportationRequestExportVoucher(
                    transportationRequestId, null, ReportDispositionEnum.inline)).withRel("voucher")
                .expand()))
            .build();
    }

    @Operation(summary = "cancel transportation request")
    @PostMapping("{transportationRequestId}/cancelled")
    @ResponseStatus(HttpStatus.OK)
    @CanCancelRequest
    public SingleHateoasResponse<TransportationRequestResponse> cancelledRequest(
        @PathVariable("transportationRequestId") UUID transportationRequestId,
        @RequestBody CancelRequestReasonRequest request)
    {
        processTransportationRequestCancelledCmd.withRequest(
                ProcessTransportationRequestCancelledCmd.Request.builder()
                    .transportationRequestId(transportationRequestId)
                    .cancelRequestReasonRequest(request)
                    .build())
            .execute();

        return SingleHateoasResponse.<TransportationRequestResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Transportation request cancelled successfully")
            .links(List.of(linkTo(
                methodOn(TransportationRequestController.class).transportationRequestExportVoucher(
                    transportationRequestId, null, ReportDispositionEnum.inline)).withRel("voucher")
                .expand()))
            .build();
    }

    @Operation(summary = "transportation request export voucher")
    @GetMapping("{transportationRequestId}/voucher")
    @ResponseStatus(HttpStatus.OK)
    @CanReadRequest
    public ResponseEntity<byte[]> transportationRequestExportVoucher(
        @PathVariable("transportationRequestId") UUID transportationRequestId,
        @RequestParam(name = "zoneId", defaultValue = "America/La_Paz") String zoneId,
        @RequestParam(name = "disposition",
                      defaultValue = "inline") ReportDispositionEnum disposition)
    {
        return pdfExportCmd.withRequest(PdfExportCmd.Request.builder()
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
