package com.kernotec.driverschedule.report.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TripType {
    ONE_WAY("Solo Ida"), ROUND_TRIP("Ida y Vuelta");

    private final String value;

    public static TripType fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (TripType entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }
}
