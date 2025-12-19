package com.kernotec.driverscheduleservice.exception;

import static com.kernotec.driverscheduleservice.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class LocationException extends ApiException {

    private static final String template = "exception.location.%s.message";

    public LocationException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public LocationException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
