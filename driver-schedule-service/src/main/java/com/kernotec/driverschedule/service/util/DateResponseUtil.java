package com.kernotec.driverschedule.service.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class DateResponseUtil {

    @Named("mapToZonedDateTimeResponse")
    public ZonedDateTime mapToZonedDateTimeResponse(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
    }
}
