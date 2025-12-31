package com.kernotec.driverscheduleservice.rest.command.transportation.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.exception.TransportationRequestException;
import com.kernotec.driverscheduleservice.util.ZonedDateTimeUtil;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransportationRequestValidationCmd extends
    AbstractTransactionalRequiredCommand<TransportationRequestValidationCmd.Request, Void>
{

    private final ZonedDateTimeUtil zonedDateTimeUtil;

    @Override
    protected Void run(Request request) {
        ZonedDateTime fromDateToSchedule = zonedDateTimeUtil.getNewOfDateAndTime(
            request.requestedDate, request.fromDate);
        ZonedDateTime toDateToSchedule = zonedDateTimeUtil.getNewOfDateAndTime(
            request.requestedDate, request.toDate);

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
                          @NotNull ZonedDateTime requestedDate)
    {

    }
}
