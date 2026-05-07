package com.kernotec.driverschedule.socket;

public class WebSocketTopic {

    public static final String BROKER_PREFIX = "/topic";
    public static final String APP_PREFIX = "/app";
    public static final String PRIVATE_MESSAGE_PREFIX = "/queue";

    public static final String VEHICLE_CREATED = BROKER_PREFIX + "/vehicle.created";

    public static final String VEHICLE_UPDATED = BROKER_PREFIX + "/vehicle.updated";

    public static final String LOCATION_CREATED = BROKER_PREFIX + "/location.created";

    public static final String LOCATION_UPDATED = BROKER_PREFIX + "/location.updated";

    public static final String TRANSPORTATION_REQUEST_CREATED =
        BROKER_PREFIX + "/transportation.request.created";

    public static final String TRANSPORTATION_REQUEST_REJECTED =
        BROKER_PREFIX + "/transportation.request.rejected";

    public static final String TRANSPORTATION_REQUEST_REJECTED_TO_USER =
        PRIVATE_MESSAGE_PREFIX + "/transportation.request.rejected";

    public static final String TRANSPORTATION_REQUEST_CANCELLED =
        BROKER_PREFIX + "/transportation.request.cancelled";

    public static final String SCHEDULE_TRANSPORTATION_CREATED =
        BROKER_PREFIX + "/schedule.transportation.created";

    public static final String SCHEDULE_TRANSPORTATION_CREATED_TO_USER =
        PRIVATE_MESSAGE_PREFIX + "/schedule.transportation.created";

    public static final String SCHEDULE_TRANSPORTATION_RESCHEDULED =
        BROKER_PREFIX + "/schedule.transportation.rescheduled";

    public static final String SCHEDULE_TRANSPORTATION_RESCHEDULED_TO_USER =
        PRIVATE_MESSAGE_PREFIX + "/schedule.transportation.rescheduled";

    public static final String SCHEDULE_TRANSPORTATION_CANCELLED =
        BROKER_PREFIX + "/schedule.transportation.cancelled";

    public static final String SCHEDULE_TRANSPORTATION_CANCELLED_TO_USER =
        PRIVATE_MESSAGE_PREFIX + "/schedule.transportation.cancelled";

    public static final String SCHEDULE_TRANSPORTATION_ON_PROGRESS =
        BROKER_PREFIX + "/schedule.transportation.on.progress";

    public static final String SCHEDULE_TRANSPORTATION_FINALIZED =
        BROKER_PREFIX + "/schedule.transportation.finalized";

    public static final String TRIP_STARTED = BROKER_PREFIX + "/trip.started";

    public static final String TRIP_CHANGED = BROKER_PREFIX + "/trip.changed";

    public static final String TRIP_FINALIZED = BROKER_PREFIX + "/trip.finalized";

    public static final String TRIP_EMERGENCY_REPORTED = BROKER_PREFIX + "/trip.emergency.reported";

    public static final String TRIP_EMERGENCY_DISMISSED_TO_USER =
        PRIVATE_MESSAGE_PREFIX + "/trip.emergency.dismissed";

    public static final String TRIP_EMERGENCY_HANDLED_TO_USER =
        PRIVATE_MESSAGE_PREFIX + "/trip.emergency.handled";

    public static final String TEST_MESSAGE = "/test.message";

    private WebSocketTopic() {
    }
}
