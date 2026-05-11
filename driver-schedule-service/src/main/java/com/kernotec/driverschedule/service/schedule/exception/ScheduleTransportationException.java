package com.kernotec.driverschedule.service.schedule.exception;

import static com.kernotec.driverschedule.common.exception.ExceptionMessage.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class ScheduleTransportationException extends ApiException {

    private static final String template = "exception.schedule.transportation.%s.message";

    public ScheduleTransportationException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public ScheduleTransportationException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
