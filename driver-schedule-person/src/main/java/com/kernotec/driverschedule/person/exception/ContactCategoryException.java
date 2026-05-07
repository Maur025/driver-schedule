package com.kernotec.driverschedule.person.exception;

import static com.kernotec.driverschedule.common.exception.ExceptionMessage.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class ContactCategoryException extends ApiException {

    private static final String template = "exception.contact.type.%s.message";

    public ContactCategoryException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public ContactCategoryException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
