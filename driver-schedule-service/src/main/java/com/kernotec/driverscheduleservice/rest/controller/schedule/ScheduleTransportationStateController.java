package com.kernotec.driverscheduleservice.rest.controller.schedule;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportationState;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationStateService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.ScheduleTransportationStateSpec;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.schedule.transportation.state.ScheduleTransportationStateResponse;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation.state.ScheduleTransportationStateResponseMapper;
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

@Tag(name = ScheduleTransportationStateSpec.TAG_NAME,
     description = ScheduleTransportationStateSpec.TAG_DESCRIPTION)
@RequestMapping(path = ScheduleTransportationStateSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class ScheduleTransportationStateController {

    private final ScheduleTransportationStateService scheduleTransportationStateService;
    private final ScheduleTransportationStateResponseMapper scheduleTransportationStateResponseMapper;

    @Operation(summary = "find all schedule transportation states")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ScheduleTransportationStateResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<ScheduleTransportationState> scheduleTransportationStatePage = scheduleTransportationStateService.findAll(
            pageable);

        return PageResponse.<ScheduleTransportationStateResponse>builder()
            .code(HttpStatus.OK.value())
            .data(scheduleTransportationStateResponseMapper.toResponse(
                scheduleTransportationStatePage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(scheduleTransportationStatePage.getTotalElements())
                .pages(scheduleTransportationStatePage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find schedule transportation states without pagination")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Deprecated
    public PageResponse<ScheduleTransportationStateResponse> findAllWithoutPagination() {
        List<ScheduleTransportationState> scheduleTransportationStateList = scheduleTransportationStateService.findAll();

        return PageResponse.<ScheduleTransportationStateResponse>builder()
            .code(HttpStatus.OK.value())
            .data(scheduleTransportationStateResponseMapper.toResponse(
                scheduleTransportationStateList))
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{scheduleTransportationStateId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ScheduleTransportationStateResponse> findById(
        @PathVariable("scheduleTransportationStateId") UUID scheduleTransportationStateId)
    {
        ScheduleTransportationState scheduleTransportationState = scheduleTransportationStateService.findByIdThrow(
            scheduleTransportationStateId);

        return SingleResponse.<ScheduleTransportationStateResponse>builder()
            .code(HttpStatus.OK.value())
            .data(scheduleTransportationStateResponseMapper.toResponse(scheduleTransportationState))
            .build();
    }
}
