package com.kernotec.driverscheduleservice.jpa.service.resource;

import com.kernotec.core.util.MessageUtil;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationService;
import com.kernotec.driverscheduleservice.rest.dto.resource.request.AvailabilityForAssignmentRequest;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.AvailabilityForAssignmentResponse;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationResponseMapper;
import com.kernotec.driverscheduleservice.util.ZonedDateTimeUtil;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AvailabilityForAssignmentService {

    private final MessageSource messageSource;
    private final ZonedDateTimeUtil zonedDateTimeUtil;
    private final ScheduleTransportationService scheduleTransportationService;
    private final ScheduleTransportationResponseMapper scheduleTransportationResponseMapper;

    public AvailabilityForAssignmentResponse checkDriverIsAvailable(
        AvailabilityForAssignmentRequest request)
    {
        String resource = "resource.driver";

        return checkIsAvailable(
            request,
            availabilityRequestBuilder -> availabilityRequestBuilder.driverIds(request.driverIds())
                .build(), scheduleTransportationService::findConflictByDriverIds, resource
        );
    }

    public AvailabilityForAssignmentResponse checkVehicleIsAvailable(
        AvailabilityForAssignmentRequest request)
    {
        String resource = "resource.vehicle";

        return checkIsAvailable(
            request, availabilityRequestBuilder -> availabilityRequestBuilder.vehicleIds(
                    request.vehicleIds())
                .build(), scheduleTransportationService::findConflictByVehicleIds, resource
        );
    }

    private AvailabilityForAssignmentResponse checkIsAvailable(
        AvailabilityForAssignmentRequest request,
        Function<AvailabilityForAssignmentRequest.AvailabilityForAssignmentRequestBuilder, AvailabilityForAssignmentRequest> completeBuildFn,
        Function<AvailabilityForAssignmentRequest, Page<ScheduleTransportation>> getScheduleConflictsFn,
        String resource)
    {
        ZonedDateTime from = getDateNormalized(request.dateFrom());
        ZonedDateTime to = getDateNormalized(request.dateTo());

        ZonedDateTime timeNowWithMarginError = ZonedDateTime.now()
            .minusMinutes(15)
            .withSecond(0)
            .withNano(0);

        if (from.isBefore(timeNowWithMarginError)) {
            return AvailabilityForAssignmentResponse.builder()
                .hasConflict(true)
                .conflictReason(getDateTimeInvalidMessage(from, request.zoneId()))
                .build();
        }

        AvailabilityForAssignmentRequest requestToService = completeBuildFn.apply(
            AvailabilityForAssignmentRequest.builder()
                .dateFrom(from)
                .dateTo(to)
                .zoneId(request.zoneId())
                .scheduleTransportationExcludeId(request.scheduleTransportationExcludeId()));

        Page<ScheduleTransportation> scheduleTransportationPage = getScheduleConflictsFn.apply(
            requestToService);

        return AvailabilityForAssignmentResponse.builder()
            .hasConflict(!scheduleTransportationPage.isEmpty())
            .scheduleTransportationConflicts(scheduleTransportationResponseMapper.toResponse(
                scheduleTransportationPage.getContent()))
            .scheduleTransportationCount(scheduleTransportationPage.getTotalElements())
            .conflictReason(
                getScheduleConflictMessage(resource, scheduleTransportationPage.getTotalElements()))
            .build();
    }

    private ZonedDateTime getDateNormalized(ZonedDateTime dateTime) {
        return zonedDateTimeUtil.getDateScheduleNormalized(dateTime);
    }

    private String getDateTimeInvalidMessage(ZonedDateTime dateTime, String zoneId) {
        Locale locale = getLocale();
        ZoneId clientZoneId = ZonedDateTimeUtil.getClientZoneId(zoneId);

        String dateFormat = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", locale)
            .withZone(clientZoneId));

        return MessageUtil.getMessageError(
            "exception.availability.date.time.invalid.message",
            new Object[]{dateFormat}, locale, messageSource
        );
    }

    private String getScheduleConflictMessage(String source, long scheduleFoundCount) {
        if (scheduleFoundCount < 1) {
            return null;
        }

        Locale locale = getLocale();

        String resourceTraslation = MessageUtil.getMessage(
            source, new Object[]{}, locale, messageSource);

        return MessageUtil.getMessageError(
            "exception.availability.schedule.found.conflict.message",
            new Object[]{resourceTraslation, scheduleFoundCount}, locale, messageSource
        );
    }

    private Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
}
