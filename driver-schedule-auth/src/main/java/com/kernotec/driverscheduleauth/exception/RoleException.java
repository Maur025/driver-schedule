package com.kernotec.driverscheduleauth.exception;

import static com.kernotec.driverscheduleauth.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class RoleException extends ApiException {

    private static final String template = "exception.role.%s.message";

    public RoleException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public RoleException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
