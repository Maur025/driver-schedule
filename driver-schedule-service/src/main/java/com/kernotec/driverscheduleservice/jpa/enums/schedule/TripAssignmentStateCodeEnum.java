package com.kernotec.driverscheduleservice.jpa.enums.schedule;

public enum TripAssignmentStateCodeEnum {
    ACTIVE, REMOVED;

    public static TripAssignmentStateCodeEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (TripAssignmentStateCodeEnum entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }
}
