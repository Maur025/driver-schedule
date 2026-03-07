package com.kernotec.driverscheduleauth.exception;

import static com.kernotec.driverscheduleauth.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class ClientException extends ApiException {

    private static final String template = "exception.client.%s.message";

    public ClientException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public ClientException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
