package com.kernotec.driverschedule.person.jpa.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PersonTypeEnum {
    ADMIN("administrator"), DRIVER("Conductor"), SCHEDULER("Planificador"), APPLICANT(
        "Solicitante");

    private final String valueEs;

    public static PersonTypeEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (PersonTypeEnum entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }

    public static PersonTypeEnum getByValueEs(String valueEs) {
        if (valueEs == null || valueEs.isBlank()) {
            return null;
        }

        for (PersonTypeEnum entry : values()) {
            if (entry.getValueEs() == null || entry.getValueEs()
                .isBlank())
            {
                continue;
            }

            if (entry.getValueEs()
                .equalsIgnoreCase(valueEs))
            {
                return entry;
            }
        }

        return null;
    }
}
