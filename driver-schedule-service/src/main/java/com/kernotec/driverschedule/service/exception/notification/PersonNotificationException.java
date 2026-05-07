package com.kernotec.driverschedule.service.exception.notification;

import static com.kernotec.driverschedule.service.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class PersonNotificationException extends ApiException {

    private static final String template = "exception.person.notification.%s.message";

    public PersonNotificationException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public PersonNotificationException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
