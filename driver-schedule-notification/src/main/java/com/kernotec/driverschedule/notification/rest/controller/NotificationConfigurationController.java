package com.kernotec.driverschedule.notification.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.MessageResponse;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.notification.jpa.entitiy.NotificationConfiguration;
import com.kernotec.driverschedule.notification.jpa.service.NotificationConfigurationService;
import com.kernotec.driverschedule.notification.push.NotificationOrchestrator;
import com.kernotec.driverschedule.notification.rest.NotificationApiSpec.NotificationConfigurationSpec;
import com.kernotec.driverschedule.notification.rest.command.ProcessNotificationConfigDeleteRequestCmd;
import com.kernotec.driverschedule.notification.rest.command.ProcessNotificationConfigurationCreateRequestCmd;
import com.kernotec.driverschedule.notification.rest.dto.request.NotificationConfigurationCreateRequest;
import com.kernotec.driverschedule.notification.rest.dto.request.NotificationSendRequest;
import com.kernotec.driverschedule.notification.rest.dto.request.NotificationSendToTestRequest;
import com.kernotec.driverschedule.notification.rest.dto.response.NotificationConfigurationResponse;
import com.kernotec.driverschedule.notification.rest.mapper.response.NotificationConfigurationResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = NotificationConfigurationSpec.TAG_NAME,
     description = NotificationConfigurationSpec.TAG_DESCRIPTION)
@RequestMapping(path = NotificationConfigurationSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class NotificationConfigurationController {

    private final NotificationConfigurationService notificationConfigurationService;
    private final NotificationConfigurationResponseMapper notificationConfigurationResponseMapper;
    private final ProcessNotificationConfigurationCreateRequestCmd processNotificationConfigurationCreateRequestCmd;
    private final NotificationOrchestrator notificationOrchestrator;
    private final ProcessNotificationConfigDeleteRequestCmd processNotificationConfigDeleteRequestCmd;

    @Operation(summary = "find all notification configurations")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<NotificationConfigurationResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "false") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<NotificationConfiguration> notificationConfigurationPage = notificationConfigurationService.findAll(
            pageable);

        return PageResponse.<NotificationConfigurationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(notificationConfigurationResponseMapper.toResponse(
                notificationConfigurationPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(notificationConfigurationPage.getTotalElements())
                .pages(notificationConfigurationPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{notificationConfigurationId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<NotificationConfigurationResponse> findById(
        @PathVariable("notificationConfigurationId") UUID notificationConfigurationId)
    {
        NotificationConfiguration notificationConfiguration = notificationConfigurationService.findByIdThrow(
            notificationConfigurationId);

        return SingleResponse.<NotificationConfigurationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(notificationConfigurationResponseMapper.toResponse(notificationConfiguration))
            .build();
    }

    @Operation(summary = "save notification configuration")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResponse<NotificationConfigurationResponse> save(
        @RequestBody NotificationConfigurationCreateRequest request)
    {
        UUID notificationConfigurationId = processNotificationConfigurationCreateRequestCmd.withRequest(
                ProcessNotificationConfigurationCreateRequestCmd.Request.builder()
                    .notificationConfigurationCreateRequest(request)
                    .build())
            .execute();

        return SingleResponse.<NotificationConfigurationResponse>builder()
            .code(HttpStatus.CREATED.value())
            .data(notificationConfigurationResponseMapper.toResponse(notificationConfigurationId))
            .build();
    }

    @Operation(summary = "remove notification configuration")
    @DeleteMapping("token/{token}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse delete(@PathVariable("token") String token)
    {
        processNotificationConfigDeleteRequestCmd.withRequest(
                ProcessNotificationConfigDeleteRequestCmd.Request.builder()
                    .token(token)
                    .build())
            .execute();

        return MessageResponse.builder()
            .code(HttpStatus.OK.value())
            .message("remove successfully")
            .build();
    }

    @Operation(summary = "test send notification")
    @PostMapping("test-notification")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse testSendNotification(@RequestBody NotificationSendToTestRequest request)
    {
        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(request.getTitle())
            .body(request.getBody())
            .dataMap(request.getDataMap())
            .personIds(request.getPersonIds())
            .campaignRecipient(request.getCampaignRecipient())
            .build());

        return MessageResponse.builder()
            .code(HttpStatus.OK.value())
            .message("Send notification sucessful")
            .build();
    }
}


