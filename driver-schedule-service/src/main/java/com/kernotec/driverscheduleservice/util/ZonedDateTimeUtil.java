package com.kernotec.driverscheduleservice.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ZonedDateTimeUtil {

    public ZonedDateTime getNewOfDateAndTime(ZonedDateTime date, ZonedDateTime time) {
        LocalDate dataAsLocalDate = date.toLocalDate();
        LocalTime timeAsLocalTime = time.toLocalTime();

        return ZonedDateTime.of(dataAsLocalDate, timeAsLocalTime, time.getZone());
    }
}
