package com.kernotec.driverschedule.service.scheduling.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernotec.driverschedule.common.audit.user.json.AuthUserData;
import com.kernotec.driverschedule.common.datetime.ZonedDateTimeService;
import com.kernotec.driverschedule.report.dto.VoucherContactDto;
import com.kernotec.driverschedule.report.dto.VoucherReasonDto;
import com.kernotec.driverschedule.report.enums.ContactLabelType;
import com.kernotec.driverschedule.report.enums.ReasonCode;
import com.kernotec.driverschedule.report.enums.RequestState;
import com.kernotec.driverschedule.report.enums.ScheduleState;
import com.kernotec.driverschedule.report.enums.TripType;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VoucherCommonService {

    public static String getVoucherLabelRequestState(String status) {
        if (status == null) {
            return "SIN ESTADO";
        }

        RequestState requestState = RequestState.fromValue(status);

        return requestState.getValue();
    }

    public static String getVoucherLabelScheduleState(String status) {
        if (status == null) {
            return "SIN ESTADO";
        }

        ScheduleState scheduleState = ScheduleState.fromValue(status);

        return scheduleState.getValue();
    }

    public static String formatDateToVoucher(Timestamp timestamp, String zoneId) {
        if (timestamp == null) {
            return "N/A";
        }

        ZoneId clientZoneId = ZonedDateTimeService.getClientZoneId(zoneId);

        var zonedDateTime = ZonedDateTime.ofInstant(timestamp.toInstant(), clientZoneId);

        return zonedDateTime.format(
            DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a", Locale.ENGLISH));
    }

    public static String getPersonFullName(String name, String lastName) {
        if (name == null || name.isBlank()) {
            return "N/A";
        }

        var stringBuilder = new StringBuilder();
        stringBuilder.append(name);

        if (lastName != null && !lastName.isBlank()) {
            stringBuilder.append(" ")
                .append(lastName);
        }

        return stringBuilder.toString();
    }

    public static String getRequestTripType(String tripType) {
        if (tripType == null || tripType.isBlank()) {
            return "N/A";
        }

        TripType tripTypeEnum = TripType.fromValue(tripType);

        return tripTypeEnum.getValue();
    }

    public static String getRequestWithAdvance(Boolean shortNotice) {
        if (shortNotice == null) {
            return "N/A";
        }

        return shortNotice ? "No" : "Sí";
    }

    public static String getRequestAssetPickup(Boolean assetPickup) {
        if (assetPickup == null) {
            return "N/A";
        }

        return assetPickup ? "Sí" : "No";
    }

    public static Boolean isShowRequestReasonMessage(String requestState,
        Long cancelRequestReasonCount, Long rejectRequestReasonCount)
    {
        if (requestState == null || rejectRequestReasonCount == null
            || cancelRequestReasonCount == null)
        {
            return false;
        }

        if (RequestState.REQUESTED.equals(RequestState.fromValue(requestState))) {
            return false;
        }

        return cancelRequestReasonCount > 0 || rejectRequestReasonCount > 0;
    }

    public static Boolean isShowScheduleReasonMessage(String scheduleState, Long cancelReasonCount,
        Long rescheduleReasonCount)
    {
        if (scheduleState == null || cancelReasonCount == null || rescheduleReasonCount == null) {
            return false;
        }

        if (ScheduleState.SCHEDULED.equals(ScheduleState.fromValue(scheduleState))) {
            return false;
        }

        return cancelReasonCount > 0 || rescheduleReasonCount > 0;
    }

    public static String getDateWithConcatDateAndTime(Timestamp dateTime, String zoneId)
    {
        if (dateTime == null) {
            return "N/A";
        }

        ZoneId clientZoneId = ZonedDateTimeService.getClientZoneId(zoneId);

        var timeZonedDateTime = ZonedDateTime.ofInstant(dateTime.toInstant(), ZoneOffset.UTC);

        ZonedDateTime clientTimeWithSameUserZoneId = timeZonedDateTime.withSecond(0)
            .withNano(0)
            .withZoneSameInstant(clientZoneId);

        return clientTimeWithSameUserZoneId.format(
            DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a", Locale.ENGLISH));
    }

    public static String getRequestedByFullName(String createdByUserStr) {
        if (createdByUserStr == null || createdByUserStr.isBlank() || createdByUserStr.equals(
            "{}"))
        {
            return "N/A";
        }

        AuthUserData requestedByDto = getObjectFromString(
            createdByUserStr, new TypeReference<>() {
            }
        );

        return requestedByDto.getName();
    }

    public static String getPersonPhoneContacts(String phoneContactsStr) {
        if (phoneContactsStr == null || phoneContactsStr.isBlank() || phoneContactsStr.equals(
            "[]"))
        {
            return "N/A";
        }

        List<VoucherContactDto> voucherContactDtoList = getObjectFromString(
            phoneContactsStr, new TypeReference<>() {
            }
        );

        List<String> phoneValues = new ArrayList<>();

        for (VoucherContactDto voucherContactDto : voucherContactDtoList) {
            ContactLabelType contactType = ContactLabelType.fromValue(voucherContactDto.getLabel());

            if (!ContactLabelType.MAIN.equals(contactType) && !ContactLabelType.WORK.equals(
                contactType))
            {
                continue;
            }

            phoneValues.add(voucherContactDto.getValue());
        }

        return String.join(" - ", phoneValues);
    }

    private static <O> O getObjectFromString(String value, TypeReference<O> typeReference) {
        var objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(value, typeReference);
        } catch (JsonProcessingException ex) {
            log.error("Error parsing string to object. String value: {}", value, ex);
            throw new RuntimeException(ex);
        }
    }

    public static String getRequestReasonMessages(String requestState, String cancelReasonsStr,
        String rejectReasonsStr)
    {

        boolean isValidCancelReason = cancelReasonsStr == null || cancelReasonsStr.equals("[]")
            || cancelReasonsStr.isBlank();

        boolean isValidRejectReason =
            rejectReasonsStr == null || rejectReasonsStr.isBlank() || rejectReasonsStr.equals("[]");

        if (requestState == null || requestState.isBlank() || (isValidCancelReason
            && isValidRejectReason))
        {
            return "N/A";
        }

        List<VoucherReasonDto> reasonDtoList = switch (RequestState.fromValue(requestState)) {
            case REJECTED -> getReasonDtoList(rejectReasonsStr);
            case CANCELLED -> getReasonDtoList(cancelReasonsStr);
            default -> List.of();
        };

        if (reasonDtoList.isEmpty()) {
            return "N/A";
        }

        List<String> reasonMessages = new ArrayList<>();

        for (VoucherReasonDto voucherReasonDto : reasonDtoList) {
            ReasonCode reasonCode = ReasonCode.fromValue(voucherReasonDto.getReasonCode());

            if (reasonCode.equals(ReasonCode.OTHER_REJECT) || reasonCode.equals(
                ReasonCode.OTHER_REQUEST_CANCELLED))
            {
                reasonMessages.add(
                    Objects.requireNonNullElse(voucherReasonDto.getOtherReason(), "N/A"));
                continue;
            }

            reasonMessages.add(voucherReasonDto.getReasonLabel());
        }

        return String.join(", ", reasonMessages);
    }

    public static String getScheduleReasonMessage(String scheduleState, String cancelReasonsStr,
        String rescheduleReasonStr)
    {
        boolean isValidRescheduleReason =
            rescheduleReasonStr == null || rescheduleReasonStr.isBlank()
                || rescheduleReasonStr.equals("[]");

        boolean isValidCancelReason = cancelReasonsStr == null || cancelReasonsStr.equals("[]")
            || cancelReasonsStr.isBlank();

        if (scheduleState == null || scheduleState.isBlank() || (isValidCancelReason
            && isValidRescheduleReason))
        {
            return "N/A";
        }

        List<VoucherReasonDto> reasonDtoList = getReasonDtoList(rescheduleReasonStr);
        List<VoucherReasonDto> cancelReasonList = getReasonDtoList(cancelReasonsStr);

        if (!cancelReasonList.isEmpty()) {
            reasonDtoList.addAll(cancelReasonList);
        }

        if (reasonDtoList.isEmpty()) {
            return "N/A";
        }

        List<String> reasonMessages = new ArrayList<>();

        for (VoucherReasonDto voucherReasonDto : reasonDtoList) {
            ReasonCode reasonCode = ReasonCode.fromValue(voucherReasonDto.getReasonCode());

            if (reasonCode.equals(ReasonCode.OTHER_SCHEDULE_CANCELLED) || reasonCode.equals(
                ReasonCode.OTHER_RESCHEDULED))
            {
                reasonMessages.add(
                    Objects.requireNonNullElse(voucherReasonDto.getOtherReason(), "N/A"));
                continue;
            }

            reasonMessages.add(voucherReasonDto.getReasonLabel());
        }

        return String.join(", ", reasonMessages);
    }

    private static List<VoucherReasonDto> getReasonDtoList(String reasonListStr) {
        return getObjectFromString(
            reasonListStr, new TypeReference<>() {
            }
        );
    }

    public static String getReasonLabelMessage(String stateCodeStr, boolean isRequest) {
        if (stateCodeStr == null || stateCodeStr.isBlank()) {
            return "Motivo:";
        }

        var labelBuilder = new StringBuilder("Motivo");

        if (isRequest) {
            var stateCode = RequestState.fromValue(stateCodeStr);
            return labelBuilder.append(getReasonLabelByRequestState(stateCode))
                .toString();
        }

        var stateCode = ScheduleState.fromValue(stateCodeStr);
        return labelBuilder.append(getReasonLabelByScheduleState(stateCode))
            .toString();
    }

    private static String getReasonLabelByRequestState(RequestState stateCode) {
        return switch (stateCode) {
            case CANCELLED -> " de Cancelación:";
            case REJECTED -> " de Rechazo:";
            default -> ":";
        };
    }

    private static String getReasonLabelByScheduleState(ScheduleState stateCode) {
        return switch (stateCode) {
            case CANCELLED -> " de Cancelación:";
            case RESCHEDULED -> " de Reprogramación:";
            default -> ":";
        };
    }
}
