package com.kernotec.driverschedule.report.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestState {
    REQUESTED("SOLICITADO"),
    REJECTED("RECHAZADO"),
    CANCELLED("CANCELADO"),
    APPROVED("APROBADO");

    private final String value;

    public static RequestState fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (RequestState entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }
}
