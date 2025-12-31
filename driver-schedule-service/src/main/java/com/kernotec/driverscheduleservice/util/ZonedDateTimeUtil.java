package com.kernotec.driverscheduleservice.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ZonedDateTimeUtil {

    public static ZoneId getClientZoneId(String clientZoneId) {
        if (clientZoneId != null) {
            return ZoneId.of(clientZoneId);
        }

        return ZoneId.systemDefault();
    }

    public ZonedDateTime getNewOfDateAndTime(ZonedDateTime date, ZonedDateTime time) {
        LocalDate dataAsLocalDate = date.toLocalDate();
        ZonedDateTime timeWithoutSeconds = time.withSecond(0)
            .withNano(0);

        LocalTime timeAsLocalTime = timeWithoutSeconds.toLocalTime();

        return ZonedDateTime.of(dataAsLocalDate, timeAsLocalTime, time.getZone());
    }
}
