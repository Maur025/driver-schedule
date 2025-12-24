package com.kernotec.driverscheduleservice.exception;

import static com.kernotec.driverscheduleservice.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class ScheduleTransportationStateException extends ApiException {

    private static final String template = "exception.schedule.transportation.state.%s.message";

    public ScheduleTransportationStateException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public ScheduleTransportationStateException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
