package com.kernotec.driverscheduleservice.jpa.enums.schedule;

public enum ScheduleTransportationStateEnum {
    SCHEDULED, CANCELLED, RESCHEDULED, IN_PROGRESS, FINALIZED;

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
