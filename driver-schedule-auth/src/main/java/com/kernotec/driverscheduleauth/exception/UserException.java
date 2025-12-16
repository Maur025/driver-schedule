package com.kernotec.driverscheduleauth.exception;

import static com.kernotec.driverscheduleauth.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class UserException extends ApiException {

    private static final String template = "exception.user.%s,message";

    public UserException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public UserException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
