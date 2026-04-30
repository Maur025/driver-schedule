package com.kernotec.driverscheduleservice.rest.controller.notification;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.MessageResponse;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.jpa.entity.notification.NotificationConfiguration;
import com.kernotec.driverscheduleservice.jpa.service.notification.NotificationConfigurationService;
import com.kernotec.driverscheduleservice.notification.dto.NotificationSendRequest;
import com.kernotec.driverscheduleservice.notification.service.NotificationOrchestrator;
import com.kernotec.driverscheduleservice.rest.ApiSpec.NotificationConfigurationSpec;
import com.kernotec.driverscheduleservice.rest.command.notification.ProcessNotificationConfigurationCreateRequestCmd;
import com.kernotec.driverscheduleservice.rest.dto.notification.request.NotificationConfigurationCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.notification.request.NotificationSendToTestRequest;
import com.kernotec.driverscheduleservice.rest.dto.notification.response.NotificationConfigurationResponse;
import com.kernotec.driverscheduleservice.rest.mapper.notification.response.NotificationConfigurationResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "test send notification")
    @PostMapping("test-notification")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse testSendNotification(@RequestBody NotificationSendToTestRequest request)
    {

        notificationOrchestrator.sendAsyncNotification(NotificationOrchestrator.Request.builder()
            .notificationSendRequest(NotificationSendRequest.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .dataMap(request.getDataMap())
                .personIds(request.getPersonIds())
                .campaignRecipient(request.getCampaignRecipient())
                .build())
            .build());

        return MessageResponse.builder()
            .code(HttpStatus.OK.value())
            .message("Send notification sucessful")
            .build();
    }
}


