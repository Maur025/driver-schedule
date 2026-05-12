package com.kernotec.driverschedule.service.schedule.exception;

import static com.kernotec.driverschedule.common.exception.ExceptionMessage.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class TripAssignmentStateException extends ApiException {

    private static final String template = "exception.trip.assignment.state.%s.message";

    public TripAssignmentStateException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public TripAssignmentStateException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
