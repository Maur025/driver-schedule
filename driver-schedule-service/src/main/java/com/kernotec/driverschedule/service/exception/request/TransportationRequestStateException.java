package com.kernotec.driverschedule.service.exception.request;

import static com.kernotec.driverschedule.service.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class TransportationRequestStateException extends ApiException {

    private static final String template = "exception.transportation.request12.state.%s.message";

    public TransportationRequestStateException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public TransportationRequestStateException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
