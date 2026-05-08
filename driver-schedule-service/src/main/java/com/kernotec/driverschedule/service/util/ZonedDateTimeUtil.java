package com.kernotec.driverschedule.service.util;

import com.kernotec.core.exception.custom.base.DefaultApiException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ZonedDateTimeUtil {

    public static ZoneId getClientZoneId(String clientZoneId) {
        if (clientZoneId != null) {
            return ZoneId.of(clientZoneId);
        }

        return ZoneId.systemDefault();
    }

    public static boolean isSameDay(ZonedDateTime dateTime, String zoneId) {
        ZoneId clientZoneId = getClientZoneId(zoneId);

        ZonedDateTime dateTimeUtc = dateTime.withZoneSameInstant(ZoneOffset.UTC);

        ZonedDateTime now = ZonedDateTime.now();
        LocalDate nowLocalDate = now.toLocalDate();

        ZonedDateTime nowStartOfDay = nowLocalDate.atStartOfDay(clientZoneId);
        ZonedDateTime nowStartOfDayUtc = nowStartOfDay.withZoneSameInstant(ZoneOffset.UTC);

        ZonedDateTime nowEndOfDay = nowLocalDate.atTime(LocalTime.MAX)
            .atZone(clientZoneId);
        ZonedDateTime nowEndOfDayUtc = nowEndOfDay.withZoneSameInstant(ZoneOffset.UTC);

        return dateTimeUtc.isAfter(nowStartOfDayUtc) && dateTimeUtc.isBefore(nowEndOfDayUtc);
    }

    public ZonedDateTime getNewOfDateAndTime(ZonedDateTime date, ZonedDateTime time, String zoneId)
    {
        ZoneId clientZoneId = getClientZoneId(zoneId);

        ZonedDateTime dateWithClientZoneId = date.withZoneSameInstant(clientZoneId);

        LocalDate localDateInClientZoneId = dateWithClientZoneId.toLocalDate();

        ZonedDateTime timeWithoutSeconds = time.withSecond(0)
            .withNano(0);

        LocalTime timeAsLocalTime = timeWithoutSeconds.withZoneSameInstant(clientZoneId)
            .toLocalTime();

        ZonedDateTime joinWithUserZone = ZonedDateTime.of(
            localDateInClientZoneId, timeAsLocalTime, clientZoneId);

        return joinWithUserZone.withZoneSameInstant(ZoneOffset.UTC);
    }

    public ZonedDateTime getDateScheduleNormalized(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            throw new DefaultApiException(
                "param.not.null", "dateTime", HttpStatus.BAD_REQUEST.value());
        }

        return zonedDateTime.withSecond(0)
            .withNano(0)
            .withZoneSameInstant(ZoneOffset.UTC);
    }

    public ZonedDateTime getDateWithSameUserZone(ZonedDateTime zonedDateTime, String zoneId) {
        ZoneId clientZoneId = getClientZoneId(zoneId);

        return zonedDateTime.withZoneSameInstant(clientZoneId);
    }
}
