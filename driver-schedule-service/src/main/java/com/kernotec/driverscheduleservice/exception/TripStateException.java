package com.kernotec.driverscheduleservice.exception;

import static com.kernotec.driverscheduleservice.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class TripStateException extends ApiException {

    private static final String template = "exception.trip.state.%s.message";

    public TripStateException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public TripStateException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
