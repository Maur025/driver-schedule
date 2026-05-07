package com.kernotec.driverschedule.service.jpa.enums.resource;

public enum VehicleTypeEnum {
    PICKUP, WAGON;

    public static VehicleTypeEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (VehicleTypeEnum entry : values()) {
            if (String.valueOf(entry)
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }
}
