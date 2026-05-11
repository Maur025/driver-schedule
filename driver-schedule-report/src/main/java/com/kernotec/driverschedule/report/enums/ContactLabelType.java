package com.kernotec.driverschedule.report.enums;

public enum ContactLabelType {
    WORK, MAIN, OTHER;

    public static ContactLabelType fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (ContactLabelType entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }
}
