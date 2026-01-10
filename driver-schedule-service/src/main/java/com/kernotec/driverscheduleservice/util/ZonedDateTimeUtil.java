package com.kernotec.driverscheduleservice.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public ZonedDateTime getNewOfDateAndTime(LocalDateTime date, ZonedDateTime time, String zoneId)
    {
        ZoneId clientZoneId = getClientZoneId(zoneId);

        ZonedDateTime timeWithoutSeconds = time.withSecond(0)
            .withNano(0);

        LocalTime timeAsLocalTime = timeWithoutSeconds.withZoneSameInstant(clientZoneId)
            .toLocalTime();

        log.info("Value of  timeAsLocalTime: {}", timeAsLocalTime);

        return ZonedDateTime.of(date.toLocalDate(), timeAsLocalTime, clientZoneId);
    }
}
