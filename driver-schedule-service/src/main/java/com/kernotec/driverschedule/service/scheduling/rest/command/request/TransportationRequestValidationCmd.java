package com.kernotec.driverschedule.service.scheduling.rest.command.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.exception.TransportationRequestException;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransportationRequestValidationCmd extends
    AbstractTransactionalRequiredCommand<TransportationRequestValidationCmd.Request, Void>
{

    private final com.kernotec.driverschedule.common.datetime.ZonedDateTimeService zonedDateTimeUtil;

    @Override
    protected Void run(Request request) {
        ZonedDateTime fromDateToSchedule = zonedDateTimeUtil.getDateScheduleNormalized(
            request.fromDate);
        ZonedDateTime toDateToSchedule = zonedDateTimeUtil.getDateScheduleNormalized(
            request.toDate);

        if (toDateToSchedule.isBefore(fromDateToSchedule) || toDateToSchedule.isEqual(
            fromDateToSchedule))
        {
            throw new TransportationRequestException(
                "to.less.or.equal.from", "", HttpStatus.BAD_REQUEST.value());
        }

        ZonedDateTime timeNow = ZonedDateTime.now();

        if (fromDateToSchedule.isBefore(timeNow)) {
            throw new TransportationRequestException(
                "invalid.range.date", "", HttpStatus.BAD_REQUEST.value());
        }

        return null;
    }

    @Builder
    public record Request(@NotNull ZonedDateTime fromDate, @NotNull ZonedDateTime toDate,
                          String zoneId)
    {

    }
}
