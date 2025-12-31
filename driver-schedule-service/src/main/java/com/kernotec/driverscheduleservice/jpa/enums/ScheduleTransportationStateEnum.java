package com.kernotec.driverscheduleservice.jpa.enums;

public enum ScheduleTransportationStateEnum {
    SCHEDULED, CANCELLED, RESCHEDULED;

    public static ScheduleTransportationStateEnum fromValue(String value) {
        if (value == null) {
            return null;
        }

        for (ScheduleTransportationStateEnum entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }
}
