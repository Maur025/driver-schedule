package com.kernotec.driverscheduleservice.jpa.enums.request;

public enum TransportationRequestStateEnum {
    REQUESTED, APPROVED, REJECTED, CANCELLED;

    public static TransportationRequestStateEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (TransportationRequestStateEnum entry : values()) {
            if (String.valueOf(entry)
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }
}
