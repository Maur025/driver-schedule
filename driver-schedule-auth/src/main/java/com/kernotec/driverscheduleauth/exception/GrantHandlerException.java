package com.kernotec.driverscheduleauth.exception;

import static com.kernotec.driverscheduleauth.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class GrantHandlerException extends ApiException {

    private static final String template = "exception.grant.handler.%s.message";

    public GrantHandlerException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public GrantHandlerException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
