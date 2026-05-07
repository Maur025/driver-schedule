package com.kernotec.driverschedule.notification.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.notification.jpa.entitiy.PersonNotification;
import com.kernotec.driverschedule.notification.jpa.enums.PersonNotificationState;
import com.kernotec.driverschedule.notification.jpa.service.PersonNotificationService;
import com.kernotec.driverschedule.notification.rest.NotificationApiSpec.PersonNotificationSpec;
import com.kernotec.driverschedule.notification.rest.command.ProcessPersonNotificationUpdateRequestCmd;
import com.kernotec.driverschedule.notification.rest.dto.response.PersonNotificationCountResponse;
import com.kernotec.driverschedule.notification.rest.dto.response.PersonNotificationResponse;
import com.kernotec.driverschedule.notification.rest.mapper.response.PersonNotificationResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = PersonNotificationSpec.TAG_NAME, description = PersonNotificationSpec.TAG_DESCRIPTION)
@RequestMapping(path = PersonNotificationSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class PersonNotificationController {

    private final PersonNotificationService personNotificationService;
    private final PersonNotificationResponseMapper personNotificationResponseMapper;
    private final ProcessPersonNotificationUpdateRequestCmd processPersonNotificationUpdateRequestCmd;

    @Operation(summary = "find all notifications")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<PersonNotificationResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending,
        @RequestParam(name = "notificationStates",
                      required = false) PersonNotificationState[] notificationStates)
    {
        Set<PersonNotificationState> states = notificationStates != null ? Set.of(
            notificationStates) : null;

        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<PersonNotification> personNotificationPage = personNotificationService.findAllBySearch(
            states, pageable);

        return PageResponse.<PersonNotificationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(personNotificationResponseMapper.toResponse(personNotificationPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(personNotificationPage.getTotalElements())
                .pages(personNotificationPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "count all notifications")
    @GetMapping("count")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<PersonNotificationCountResponse> findAll(
        @RequestParam(name = "notificationStates",
                      required = false) PersonNotificationState[] notificationStates)
    {
        Set<PersonNotificationState> states = notificationStates != null ? Set.of(
            notificationStates) : null;

        Long personNotificationCount = personNotificationService.countAllBySearch(states);

        return SingleResponse.<PersonNotificationCountResponse>builder()
            .code(HttpStatus.OK.value())
            .data(PersonNotificationCountResponse.builder()
                .count(personNotificationCount)
                .personNotificationStates(notificationStates)
                .build())
            .build();
    }

    @Operation(summary = "mark as read")
    @PatchMapping("{personNotificationId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<PersonNotificationResponse> markAsRead(
        @PathVariable("personNotificationId") UUID personNotificationId)
    {
        processPersonNotificationUpdateRequestCmd.withRequest(
                ProcessPersonNotificationUpdateRequestCmd.Request.builder()
                    .personNotificationId(personNotificationId)
                    .build())
            .execute();

        return SingleResponse.<PersonNotificationResponse>builder()
            .code(HttpStatus.OK.value())
            .message("update successfully")
            .build();
    }
}
