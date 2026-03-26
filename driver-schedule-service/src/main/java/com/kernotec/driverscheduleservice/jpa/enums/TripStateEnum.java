package com.kernotec.driverscheduleservice.jpa.enums;

public enum TripStateEnum {
    PENDING, ON_ROUTE, WAITING, FINALIZED, EMERGENCY, SYSTEM_CLOSED;

    public static TripStateEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (TripStateEnum entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }
}
