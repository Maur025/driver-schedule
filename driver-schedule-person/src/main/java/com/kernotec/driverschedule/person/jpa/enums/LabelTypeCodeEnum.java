package com.kernotec.driverschedule.person.jpa.enums;

public enum LabelTypeCodeEnum {
    WORK, MAIN, OTHER;

    public static LabelTypeCodeEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (LabelTypeCodeEnum entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }
}
