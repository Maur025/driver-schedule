package com.kernotec.driverschedule.notification.notification.enums;

public enum NotificationErrorCode {
    THIRD_PARTY_AUTH_ERROR,
    INVALID_ARGUMENT,
    INTERNAL,
    QUOTA_EXCEEDED,
    SENDER_ID_MISMATCH,
    UNAVAILABLE,
    UNREGISTERED,
    INTERNAL_SERVER_ERROR;

    public static NotificationErrorCode fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (NotificationErrorCode entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }
}
