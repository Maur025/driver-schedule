package com.kernotec.driverschedule.common.util;

public class CsvImportUtil {

    public static String getValueOfCsv(String value) {
        if (value == null || value.isBlank() || value.equalsIgnoreCase("N/A")) {
            return null;
        }

        return value.trim();
    }

    public static Double getDoubleValueOfCsv(String value) {
        String valueStr = getValueOfCsv(value);

        if (valueStr == null) {
            return null;
        }

        return Double.valueOf(valueStr);
    }

    public static Integer getIntegerValueOfCsv(String value) {
        String valueStr = getValueOfCsv(value);

        if (valueStr == null) {
            return null;
        }

        return Integer.valueOf(valueStr);
    }
}
