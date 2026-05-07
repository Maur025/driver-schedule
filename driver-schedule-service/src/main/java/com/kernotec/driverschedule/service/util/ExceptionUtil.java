package com.kernotec.driverschedule.service.util;

public class ExceptionUtil {

    public static String formatMessage(String template, String key) {
        return String.format(template, key);
    }
}
