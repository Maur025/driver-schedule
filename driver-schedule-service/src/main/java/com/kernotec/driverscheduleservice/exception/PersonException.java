package com.kernotec.driverscheduleservice.exception;

import com.kernotec.core.exception.custom.base.ApiException;

public class PersonException extends ApiException {

    public PersonException(String messageParam) {
        super("exception.default.message", messageParam);
    }
}
