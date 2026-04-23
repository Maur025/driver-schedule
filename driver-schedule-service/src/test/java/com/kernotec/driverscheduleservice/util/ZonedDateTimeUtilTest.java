package com.kernotec.driverscheduleservice.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kernotec.core.test.UnitTest;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class ZonedDateTimeUtilTest extends UnitTest {

    @InjectMocks
    private ZonedDateTimeUtil zonedDateTimeUtil;

    @Test
    @DisplayName("should be return date join with correct hours in UTC")
    void shoulBeReturnDateJoinWithCorrectHoursInUTC() {
        ZonedDateTime date = ZonedDateTime.parse("2026-04-22T16:44Z");
        ZonedDateTime timeStart = ZonedDateTime.parse("2026-04-22T17:00Z");
        String zoneId = "America/La_Paz";

        ZonedDateTime result = zonedDateTimeUtil.getNewOfDateAndTime(date, timeStart, zoneId);

        assertEquals(ZonedDateTime.parse("2026-04-22T17:00Z"), result);
    }

    @Test
    @DisplayName("should be return date join with correct hours in UTC when date is after eight")
    void shoulBeReturnDateJoinWithCorrectHoursInUTCwhenDateIsAfterEight() {
        ZonedDateTime date = ZonedDateTime.parse("2026-04-23T00:44Z");
        ZonedDateTime timeStart = ZonedDateTime.parse("2026-04-22T17:00Z");
        String zoneId = "America/La_Paz";

        ZonedDateTime result = zonedDateTimeUtil.getNewOfDateAndTime(date, timeStart, zoneId);

        assertEquals(ZonedDateTime.parse("2026-04-22T17:00Z"), result);
    }

    @Test
    @DisplayName("should be return date join with correct hours in UTC when date is start day")
    void shoulBeReturnDateJoinWithCorrectHoursInUTCwhenDateIsStartDay() {
        ZonedDateTime date = ZonedDateTime.parse("2026-04-22T04:00Z");
        ZonedDateTime timeStart = ZonedDateTime.parse("2026-04-22T17:00Z");
        String zoneId = "America/La_Paz";

        ZonedDateTime result = zonedDateTimeUtil.getNewOfDateAndTime(date, timeStart, zoneId);

        assertEquals(ZonedDateTime.parse("2026-04-22T17:00Z"), result);
    }
}