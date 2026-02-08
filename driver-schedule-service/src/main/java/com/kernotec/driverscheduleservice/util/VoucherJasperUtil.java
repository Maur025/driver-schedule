package com.kernotec.driverscheduleservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernotec.driverscheduleservice.config.KernotecApiDefinition;
import com.kernotec.driverscheduleservice.jpa.enums.LabelTypeCodeEnum;
import com.kernotec.driverscheduleservice.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.TripTypeEnum;
import com.kernotec.driverscheduleservice.util.dto.VoucherContactDto;
import java.io.IOException;
import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
        };
    }

    public static String formatDateToVoucher(Timestamp timestamp, String zoneId) {
        if (timestamp == null) {
            return "N/A";
        }

        ZoneId clientZoneId = ZonedDateTimeUtil.getClientZoneId(zoneId);

        var zonedDateTime = ZonedDateTime.ofInstant(timestamp.toInstant(), clientZoneId);

        System.out.println("Zoned date time in parameter: " + zonedDateTime);

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

    public static String getDateWithConcatDateAndTime(Timestamp date, Timestamp time, String zoneId)
    {
        if (date == null || time == null) {
            return "N/A";
        }

        ZoneId clientZoneId = ZonedDateTimeUtil.getClientZoneId(zoneId);

        var timeZonedDateTime = ZonedDateTime.ofInstant(time.toInstant(), ZoneOffset.UTC);
        var dateZonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);

        LocalTime clientTime = timeZonedDateTime.withSecond(0)
            .withNano(0)
            .withZoneSameInstant(clientZoneId)
            .toLocalTime();

        LocalDate clientDate = dateZonedDateTime.toLocalDate();

        ZonedDateTime combinedDateTime = ZonedDateTime.of(clientDate, clientTime, clientZoneId);

        return combinedDateTime.format(
            DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a", Locale.ENGLISH));
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

            if (!LabelTypeCodeEnum.MOBILE.equals(code) && !LabelTypeCodeEnum.WORK.equals(code)) {
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
