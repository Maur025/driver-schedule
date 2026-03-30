package com.kernotec.driverscheduleauth.util;

import java.util.UUID;

public class CommonUtil {

    public static String getStringOfUuid(UUID value) {
        if (value == null) {
            return null;
        }

        return String.valueOf(value);
    }

    public static UUID getUuidOfString(String value) {
        if (value == null) {
            return null;
        }

        return UUID.fromString(value);
    }
}
