package com.kernotec.driverscheduleservice.jpa.enums;

public enum ReasonCodeEnum {
    NO_VEHICLES_AVAILABLE,
    NO_DRIVERS_AVAILABLE,
    OUT_OF_OPERATING_HOURS,
    OTHER_REJECT,
    VEHICLE_MAINTENANCE,
    LOW_FUEL,
    ROAD_INCIDENT,
    OPERATIONAL_EMERGENCY,
    VEHICLE_NOT_AVAILABLE,
    DRIVER_NOT_AVAILABLE,
    OTHER_SCHEDULE_CANCELLED,
    SCHEDULE_CONFLICT,
    PRIORITY_CHANGE,
    TRAFFIC_DELAY,
    PLANNING_ADJUSTMENT,
    VEHICLE_TEMPORARILY_UNAVAILABLE,
    DRIVER_TEMPORARILY_UNAVAILABLE,
    OTHER_RESCHEDULED,
    USER_PLAN_CHANGE,
    DATE_TIME_REQUESTED_ERROR,
    SERVICE_NOT_NEEDED,
    INTERNAL_RESCHEDULING,
    DUPLICATE_REQUEST,
    ACTIVITY_CANCELLED,
    OTHER_REQUEST_CANCELLED;

    public static ReasonCodeEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (ReasonCodeEnum entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }
}
