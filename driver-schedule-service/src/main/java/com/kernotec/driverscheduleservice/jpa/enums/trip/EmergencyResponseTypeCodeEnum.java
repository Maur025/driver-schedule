package com.kernotec.driverscheduleservice.jpa.enums.trip;

public enum EmergencyResponseTypeCodeEnum {

    DISPATCH_MECHANIC, DISPATCH_TOW_TRUCK, SEND_REPLACEMENT_UNIT, DISPATCH_MOBILE_SUPPORT, OTHER;

    public static EmergencyResponseTypeCodeEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (EmergencyResponseTypeCodeEnum entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }
}
