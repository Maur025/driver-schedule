package com.kernotec.driverscheduleservice.util;

import com.kernotec.driverscheduleservice.config.KernotecApiDefinition;
import com.kernotec.driverscheduleservice.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.TripTypeEnum;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoucherJasperUtil {

    private final KernotecApiDefinition kernotecApiDefinition;

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

    public static String formatDateToVoucher(Timestamp timestamp, String zoneId) {
        if (timestamp == null) {
            return "N/A";
        }

        ZoneId clientZoneId = ZonedDateTimeUtil.getClientZoneId(zoneId);

        ZonedDateTime zonedDateTime = timestamp.toInstant()
            .atZone(clientZoneId);

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

    public String getVoucherUrl(String resource, Object... uriVariables) {
        return UriComponentsBuilder.fromHttpUrl(kernotecApiDefinition.getServers()
                .get(0)
                .getUrl())
            .path("/api/driver-schedule" + resource)
            .queryParam("disposition", "inline")
            .buildAndExpand(uriVariables)
            .toUriString();
    }
}
