package com.kernotec.driverscheduleauth.exception;

import static com.kernotec.driverscheduleauth.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class TokenException extends ApiException {

    private static final String template = "exception.token.%s.message";

    public TokenException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public TokenException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
