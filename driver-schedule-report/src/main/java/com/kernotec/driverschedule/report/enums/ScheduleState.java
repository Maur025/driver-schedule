package com.kernotec.driverschedule.report.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleState {
    SCHEDULED("SOLICITUD APROBADA"),
    RESCHEDULED("REPROGRAMADO"),
    CANCELLED("CANCELADO"),
    IN_PROGRESS("EN PROGRESO"),
    FINALIZED("FINALIZADO"),
    NEEDS_ACTION("ACCION REQUERIDA");

    private final String value;

    public static ScheduleState fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (ScheduleState entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }
}
