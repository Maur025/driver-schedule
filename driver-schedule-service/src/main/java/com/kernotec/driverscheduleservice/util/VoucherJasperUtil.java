package com.kernotec.driverscheduleservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernotec.driverscheduleservice.audit.user.json.AuthUserData;
import com.kernotec.driverscheduleservice.config.KernotecApiDefinition;
import com.kernotec.driverscheduleservice.request.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.request.jpa.enums.TripTypeEnum;
import com.kernotec.driverscheduleservice.jpa.enums.resource.LabelTypeCodeEnum;
import com.kernotec.driverscheduleservice.jpa.enums.resource.ReasonCodeEnum;
import com.kernotec.driverscheduleservice.jpa.enums.schedule.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.util.dto.VoucherContactDto;
import com.kernotec.driverscheduleservice.util.dto.VoucherReasonDto;
import java.io.IOException;
import java.net.URI;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRParameter;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoucherJasperUtil {

    private final KernotecApiDefinition kernotecApiDefinition;
    private final ResourceLoader resourceLoader;

    public static String getVoucherLabelRequestState(String status) {
        if (status == null) {
            return "SIN ESTADO";
        }

        return switch (TransportationRequestStateEnum.fromValue(status)) {
            case REQUESTED -> "SOLICITADO";
            case REJECTED -> "RECHAZADO";
            case CANCELLED -> "CANCELADO";
            case APPROVED -> "APROBADO";
        };
    }

    public static String getVoucherLabelScheduleState(String status) {
        if (status == null) {
            return "SIN ESTADO";
        }

        return switch (ScheduleTransportationStateEnum.fromValue(status)) {
            case SCHEDULED -> "SOLICITUD APROBADA";
            case RESCHEDULED -> "REPROGRAMADO";
            case CANCELLED -> "CANCELADO";
            case IN_PROGRESS -> "EN PROGRESO";
            case FINALIZED -> "FINALIZADO";
            case NEEDS_ACTION -> "ACCION REQUERIDA";
        };
    }

    public static String formatDateToVoucher(Timestamp timestamp, String zoneId) {
        if (timestamp == null) {
            return "N/A";
        }

        ZoneId clientZoneId = ZonedDateTimeUtil.getClientZoneId(zoneId);

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

        return switch (TripTypeEnum.fromValue(tripType)) {
            case ONE_WAY -> "Solo Ida";
            case ROUND_TRIP -> "Ida y Vuelta";
        };
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

        if (TransportationRequestStateEnum.REQUESTED.equals(
            TransportationRequestStateEnum.fromValue(requestState)))
        {
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

        if (ScheduleTransportationStateEnum.SCHEDULED.equals(
            ScheduleTransportationStateEnum.fromValue(scheduleState)))
        {
            return false;
        }

        return cancelReasonCount > 0 || rescheduleReasonCount > 0;
    }

    public static String getDateWithConcatDateAndTime(Timestamp dateTime, String zoneId)
    {
        if (dateTime == null) {
            return "N/A";
        }

        ZoneId clientZoneId = ZonedDateTimeUtil.getClientZoneId(zoneId);

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
            LabelTypeCodeEnum code = LabelTypeCodeEnum.fromValue(voucherContactDto.getLabel());

            if (!LabelTypeCodeEnum.MAIN.equals(code) && !LabelTypeCodeEnum.WORK.equals(code)) {
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

        List<VoucherReasonDto> reasonDtoList = switch (TransportationRequestStateEnum.fromValue(
            requestState)) {
            case REJECTED -> getReasonDtoList(rejectReasonsStr);
            case CANCELLED -> getReasonDtoList(cancelReasonsStr);
            default -> List.of();
        };

        if (reasonDtoList.isEmpty()) {
            return "N/A";
        }

        List<String> reasonMessages = new ArrayList<>();

        for (VoucherReasonDto voucherReasonDto : reasonDtoList) {
            ReasonCodeEnum reasonCode = ReasonCodeEnum.fromValue(voucherReasonDto.getReasonCode());

            if (reasonCode.equals(ReasonCodeEnum.OTHER_REJECT) || reasonCode.equals(
                ReasonCodeEnum.OTHER_REQUEST_CANCELLED))
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
            ReasonCodeEnum reasonCode = ReasonCodeEnum.fromValue(voucherReasonDto.getReasonCode());

            if (reasonCode.equals(ReasonCodeEnum.OTHER_SCHEDULE_CANCELLED) || reasonCode.equals(
                ReasonCodeEnum.OTHER_RESCHEDULED))
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
            var stateCode = TransportationRequestStateEnum.fromValue(stateCodeStr);
            return labelBuilder.append(getReasonLabelByRequestState(stateCode))
                .toString();
        }

        var stateCode = ScheduleTransportationStateEnum.fromValue(stateCodeStr);
        return labelBuilder.append(getReasonLabelByScheduleState(stateCode))
            .toString();
    }

    private static String getReasonLabelByRequestState(TransportationRequestStateEnum stateCode) {
        return switch (stateCode) {
            case CANCELLED -> " de Cancelación:";
            case REJECTED -> " de Rechazo:";
            default -> ":";
        };
    }

    private static String getReasonLabelByScheduleState(ScheduleTransportationStateEnum stateCode) {
        return switch (stateCode) {
            case CANCELLED -> " de Cancelación:";
            case RESCHEDULED -> " de Reprogramación:";
            default -> ":";
        };
    }

    public byte[] imageToByteArray(String imgResourcePath) {
        String path = "classpath:images/" + imgResourcePath;

        try {
            return resourceLoader.getResource(path)
                .getContentAsByteArray();
        } catch (IOException ex) {
            log.error("Error reading image from resource: {}", path, ex);
            throw new RuntimeException(ex);
        }
    }

    public Map<String, Object> getCommonParams(String zoneId) {
        Map<String, Object> params = new HashMap<>();

        params.put(JRParameter.REPORT_TIME_ZONE, TimeZone.getTimeZone("UTC"));
        params.put("TITLE_LOGO_IMG", imageToByteArray("logo-kerno-booking.png"));
        params.put("CLOCK_ICON_IMG", imageToByteArray("icons/clock.png"));
        params.put("USERS_ICON_IMG", imageToByteArray("icons/users.png"));
        params.put("BOX_ICON_IMG", imageToByteArray("icons/box.png"));
        params.put("PHONE_ICON_IMG", imageToByteArray("icons/phone.png"));
        params.put("ZONE_ID", zoneId);

        return params;
    }

    public String getVoucherUrl(String resource, Object... uriVariables) {
        String baseUrlStr = kernotecApiDefinition.getServers()
            .get(0)
            .getUrl();

        URI baseUri = URI.create(baseUrlStr);

        return UriComponentsBuilder.fromUri(baseUri)
            .path("/api/driver-schedule")
            .path(resource)
            .queryParam("disposition", "inline")
            .buildAndExpand(uriVariables)
            .toUriString();
    }
}
