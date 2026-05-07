package com.kernotec.driverschedule.service.exception.resource;

import static com.kernotec.driverschedule.service.util.ExceptionUtil.formatMessage;

import com.kernotec.core.exception.custom.base.ApiException;

public class VehicleException extends ApiException {

    private static final String template = "exception.vehicle.%s.message";

    public VehicleException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public VehicleException(String key, String messageParam, Integer code) {
        super(formatMessage(template, key), messageParam, code);
    }
}
