package com.kernotec.driverscheduleservice.exception.trip;

import static com.kernotec.driverscheduleservice.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class TripException extends ApiException {

    private static final String template = "exception.trip.%s.message";

    public TripException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public TripException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
