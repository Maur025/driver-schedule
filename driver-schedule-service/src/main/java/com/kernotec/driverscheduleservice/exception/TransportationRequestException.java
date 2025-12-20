package com.kernotec.driverscheduleservice.exception;

import static com.kernotec.driverscheduleservice.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class TransportationRequestException extends ApiException {

    private static final String template = "exception.transportation.request.%s.message";

    public TransportationRequestException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public TransportationRequestException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
