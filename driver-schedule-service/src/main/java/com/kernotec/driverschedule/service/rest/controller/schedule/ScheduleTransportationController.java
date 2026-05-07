package com.kernotec.driverschedule.service.rest.controller.schedule;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.common.rest.ApiSpec.ScheduleTransportationSpec;
import com.kernotec.driverschedule.service.common.annotation.schedule.transportation.CanCancelSchedule;
import com.kernotec.driverschedule.service.common.annotation.schedule.transportation.CanCreateSchedule;
import com.kernotec.driverschedule.service.common.annotation.schedule.transportation.CanReadSchedule;
import com.kernotec.driverschedule.service.common.annotation.schedule.transportation.CanReschedule;
import com.kernotec.driverschedule.service.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverschedule.service.jpa.service.schedule.ScheduleTransportationService;
import com.kernotec.driverschedule.service.report.jpa.enums.ReportDispositionEnum;
import com.kernotec.driverschedule.service.report.rest.command.pdf.PdfExportCmd;
import com.kernotec.driverschedule.service.rest.command.schedule.schedule.transportation.ProcessScheduleAddAssignmentRequestCmd;
import com.kernotec.driverschedule.service.rest.command.schedule.schedule.transportation.ProcessScheduleFinalizedRequestCmd;
import com.kernotec.driverschedule.service.rest.command.schedule.schedule.transportation.ProcessScheduleTransportationCancelRequestCmd;
import com.kernotec.driverschedule.service.rest.command.schedule.schedule.transportation.ProcessScheduleTransportationCreateRequestCmd;
import com.kernotec.driverschedule.service.rest.command.schedule.schedule.transportation.ProcessScheduleTransportationUpdateRequestCmd;
import com.kernotec.driverschedule.service.rest.command.schedule.schedule.transportation.VoucherScheduleTransportationPdfExportCmd;
import com.kernotec.driverschedule.common.response.SingleHateoasResponse;
import com.kernotec.driverschedule.service.rest.dto.schedule.request.schedule.transportation.ScheduleAddAssignmentRequest;
import com.kernotec.driverschedule.service.rest.dto.schedule.request.schedule.transportation.ScheduleTransportationCancelRequest;
import com.kernotec.driverschedule.service.rest.dto.schedule.request.schedule.transportation.ScheduleTransportationCreateRequest;
import com.kernotec.driverschedule.service.rest.dto.schedule.request.schedule.transportation.ScheduleTransportationFilterRequest;
import com.kernotec.driverschedule.service.rest.dto.schedule.request.schedule.transportation.ScheduleTransportationUpdateRequest;
import com.kernotec.driverschedule.service.rest.dto.schedule.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverschedule.service.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = ScheduleTransportationSpec.TAG_NAME,
     description = ScheduleTransportationSpec.TAG_DESCRIPTION)
@RequestMapping(path = ScheduleTransportationSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class ScheduleTransportationController {

    private final ScheduleTransportationService scheduleTransportationService;
    private final ScheduleTransportationResponseMapper scheduleTransportationResponseMapper;

    private final ProcessScheduleTransportationCreateRequestCmd processScheduleTransportationCreateRequestCmd;
    private final ProcessScheduleTransportationUpdateRequestCmd processScheduleTransportationUpdateRequestCmd;
    private final ProcessScheduleTransportationCancelRequestCmd processScheduleTransportationCancelRequestCmd;
    private final PdfExportCmd pdfExportCmd;
    private final VoucherScheduleTransportationPdfExportCmd voucherScheduleTransportationPdfExportCmd;
    private final ProcessScheduleAddAssignmentRequestCmd processScheduleAddAssignmentRequestCmd;
    private final ProcessScheduleFinalizedRequestCmd processScheduleFinalizedRequestCmd;

    @Operation(summary = "find all schedule transportations")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CanReadSchedule
    public PageResponse<ScheduleTransportationResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<ScheduleTransportation> scheduleTransportationPage = scheduleTransportationService.findAll(
            pageable);

        return PageResponse.<ScheduleTransportationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(scheduleTransportationResponseMapper.toResponse(
                scheduleTransportationPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(scheduleTransportationPage.getTotalElements())
                .pages(scheduleTransportationPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "search schedule transportation")
    @PostMapping("search")
    @ResponseStatus(HttpStatus.OK)
    @CanReadSchedule
    public PageResponse<ScheduleTransportationResponse> findAllBySearch(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending,
        @RequestBody ScheduleTransportationFilterRequest request)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<ScheduleTransportation> scheduleTransportationPage = scheduleTransportationService.findAllBySearch(
            request, pageable);

        return PageResponse.<ScheduleTransportationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(scheduleTransportationResponseMapper.toResponse(
                scheduleTransportationPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(scheduleTransportationPage.getTotalElements())
                .pages(scheduleTransportationPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find schedule transportations by id")
    @GetMapping("{scheduleTransportationId}")
    @ResponseStatus(HttpStatus.OK)
    @CanReadSchedule
    public SingleResponse<ScheduleTransportationResponse> findById(
        @PathVariable("scheduleTransportationId") UUID scheduleTransportationId)
    {
        ScheduleTransportation scheduleTransportation = scheduleTransportationService.findByIdThrow(
            scheduleTransportationId);

        return SingleResponse.<ScheduleTransportationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(scheduleTransportationResponseMapper.toResponse(scheduleTransportation))
            .build();
    }

    @Operation(summary = "save schedule transportation")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CanCreateSchedule
    public SingleHateoasResponse<ScheduleTransportationResponse> save(
        @RequestBody ScheduleTransportationCreateRequest request)
    {
        UUID scheduleTransportationId = processScheduleTransportationCreateRequestCmd.withRequest(
                ProcessScheduleTransportationCreateRequestCmd.Request.builder()
                    .scheduleTransportationCreateRequest(request)
                    .build())
            .execute();

        return SingleHateoasResponse.<ScheduleTransportationResponse>builder()
            .code(HttpStatus.CREATED.value())
            .data(scheduleTransportationResponseMapper.toResponse(scheduleTransportationId))
            .links(List.of(linkTo(methodOn(
                ScheduleTransportationController.class).scheduleTransportationExportVoucher(
                scheduleTransportationId, null, ReportDispositionEnum.inline)).withRel("voucher")
                .expand()))
            .build();
    }

    @Operation(summary = "reschedule transportation")
    @PatchMapping("{scheduleTransportationId}/rescheduled")
    @ResponseStatus(HttpStatus.OK)
    @CanReschedule
    public SingleHateoasResponse<ScheduleTransportationResponse> reschedule(
        @PathVariable("scheduleTransportationId") UUID scheduleTransportationId,
        @RequestBody ScheduleTransportationUpdateRequest request)
    {
        processScheduleTransportationUpdateRequestCmd.withRequest(
                ProcessScheduleTransportationUpdateRequestCmd.Request.builder()
                    .scheduleTransportationId(scheduleTransportationId)
                    .scheduleTransportationUpdateRequest(request)
                    .build())
            .execute();

        return SingleHateoasResponse.<ScheduleTransportationResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Reschedule successful")
            .links(List.of(linkTo(methodOn(
                ScheduleTransportationController.class).scheduleTransportationExportVoucher(
                scheduleTransportationId, null, ReportDispositionEnum.inline)).withRel("voucher")
                .expand()))
            .build();
    }

    @Operation(summary = "cancel schedule transportation")
    @PostMapping("{scheduleTransportationId}/cancelled")
    @ResponseStatus(HttpStatus.OK)
    @CanCancelSchedule
    public SingleHateoasResponse<ScheduleTransportationResponse> cancel(
        @PathVariable("scheduleTransportationId") UUID scheduleTransportationId,
        @RequestBody ScheduleTransportationCancelRequest request)
    {
        processScheduleTransportationCancelRequestCmd.withRequest(
                ProcessScheduleTransportationCancelRequestCmd.Request.builder()
                    .scheduleTransportationId(scheduleTransportationId)
                    .scheduleTransportationCancelRequest(request)
                    .build())
            .execute();

        return SingleHateoasResponse.<ScheduleTransportationResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Cancellation successful")
            .links(List.of(linkTo(methodOn(
                ScheduleTransportationController.class).scheduleTransportationExportVoucher(
                scheduleTransportationId, null, ReportDispositionEnum.inline)).withRel("voucher")
                .expand()))
            .build();
    }

    @Operation(summary = "add asignments to schedule transportation")
    @PostMapping("{scheduleTransportationId}/assignments")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ScheduleTransportationResponse> addAssignments(
        @PathVariable("scheduleTransportationId") UUID scheduleTransportationId,
        @RequestBody ScheduleAddAssignmentRequest request)
    {
        processScheduleAddAssignmentRequestCmd.withRequest(
                ProcessScheduleAddAssignmentRequestCmd.Request.builder()
                    .scheduleTransportationId(scheduleTransportationId)
                    .scheduleAddAssignmentRequest(request)
                    .build())
            .execute();

        return SingleResponse.<ScheduleTransportationResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Add assignments successful")
            .build();
    }

    @Operation(summary = "end schedule transportation")
    @PostMapping("{scheduleTransportationId}/finalized")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ScheduleTransportationResponse> finalize(
        @PathVariable("scheduleTransportationId") UUID scheduleTransportationId)
    {
        processScheduleFinalizedRequestCmd.withRequest(
                ProcessScheduleFinalizedRequestCmd.Request.builder()
                    .scheduleTransportationId(scheduleTransportationId)
                    .build())
            .execute();

        return SingleResponse.<ScheduleTransportationResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Schedule finalized with all dependencies successfully")
            .build();
    }

    @Operation(summary = "schedule transportation export voucher")
    @GetMapping("{scheduleTransportationId}/voucher")
    @CanReadSchedule
    public ResponseEntity<byte[]> scheduleTransportationExportVoucher(
        @PathVariable("scheduleTransportationId") UUID scheduleTransportationId,
        @RequestParam(name = "zoneId", defaultValue = "America/La_Paz") String zoneId,
        @RequestParam(name = "disposition",
                      defaultValue = "inline") ReportDispositionEnum disposition)
    {
        return pdfExportCmd.withRequest(PdfExportCmd.Request.builder()
                .disposition(disposition)
                .fileName("schedule_transportation_voucher")
                .callbackGetReportBytes(() -> voucherScheduleTransportationPdfExportCmd.withRequest(
                        VoucherScheduleTransportationPdfExportCmd.Request.builder()
                            .scheduleTransportationId(scheduleTransportationId)
                            .zoneId(zoneId)
                            .build())
                    .execute())
                .build())
            .execute();
    }
}
