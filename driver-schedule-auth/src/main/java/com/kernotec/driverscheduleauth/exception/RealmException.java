package com.kernotec.driverscheduleauth.exception;

import static com.kernotec.driverscheduleauth.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class RealmException extends ApiException {

    private static final String template = "exception.realm.%s.message";

    public RealmException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public RealmException(String key, String messageParam) {
        super(formatMessage(template, key), messageParam);
    }

    public RealmException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }

    public RealmException(String key, String messageParam, Integer code, String fieldName) {
        super(formatMessage(template, key), messageParam, code, fieldName);
    }

    public RealmException(String key, String messageParam, Integer code, String fieldName,
        String additionalMessage)
    {
        super(formatMessage(template, key), messageParam, code, fieldName, additionalMessage);
    }
}
