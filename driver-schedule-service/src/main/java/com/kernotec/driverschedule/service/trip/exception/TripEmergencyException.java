package com.kernotec.driverschedule.service.trip.exception;

import static com.kernotec.driverschedule.common.exception.ExceptionMessage.formatMessage;

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
