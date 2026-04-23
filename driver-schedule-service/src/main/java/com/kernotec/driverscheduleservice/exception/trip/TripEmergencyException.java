package com.kernotec.driverscheduleservice.exception.trip;

import static com.kernotec.driverscheduleservice.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class TripEmergencyException extends ApiException {

    private static final String template = "exception.trip.emergency.%s.message";

    public TripEmergencyException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public TripEmergencyException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
