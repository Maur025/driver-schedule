package com.kernotec.driverschedule.common.exception;

public class ExceptionMessage {

    public static String formatMessage(String template, String key) {
        return String.format(template, key);
    }
}
