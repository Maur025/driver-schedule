package com.kernotec.driverscheduleservice.jpa.enums;

public enum TripTypeEnum {
    ONE_WAY, ROUND_TRIP;

    public static TripTypeEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (TripTypeEnum entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }
}
