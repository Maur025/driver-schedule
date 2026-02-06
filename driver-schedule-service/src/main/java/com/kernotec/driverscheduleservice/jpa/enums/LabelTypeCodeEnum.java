package com.kernotec.driverscheduleservice.jpa.enums;

public enum LabelTypeCodeEnum {
    MOBILE, WORK, MAIN, OTHER, HOME;

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
