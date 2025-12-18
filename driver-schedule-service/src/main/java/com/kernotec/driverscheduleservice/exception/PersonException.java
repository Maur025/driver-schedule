package com.kernotec.driverscheduleservice.exception;

import static com.kernotec.driverscheduleservice.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class PersonException extends ApiException {

    private static final String template = "exception.person.%s.message";

    public PersonException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public PersonException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
