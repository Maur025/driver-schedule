package com.kernotec.driverscheduleservice.exception.trip;

import static com.kernotec.driverscheduleservice.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class TripEmergencyStateException extends ApiException {

    public static final String template = "exception.trip.emergency.state.%s.message";

    public TripEmergencyStateException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public TripEmergencyStateException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
