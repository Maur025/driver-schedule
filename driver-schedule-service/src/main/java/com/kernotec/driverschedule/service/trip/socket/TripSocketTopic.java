package com.kernotec.driverschedule.service.trip.socket;

import com.kernotec.driverschedule.socket.WebSocketTopic;

public class TripSocketTopic {

    public static final String TRIP_STARTED = WebSocketTopic.BROKER_PREFIX + "/trip.started";

    public static final String TRIP_CHANGED = WebSocketTopic.BROKER_PREFIX + "/trip.changed";

    public static final String TRIP_FINALIZED = WebSocketTopic.BROKER_PREFIX + "/trip.finalized";

    public static final String TRIP_FINALIZED_TO_USER = WebSocketTopic.PRIVATE_MESSAGE_PREFIX + "/trip.finalized";

    public static final String TRIP_EMERGENCY_REPORTED =
        WebSocketTopic.BROKER_PREFIX + "/trip.emergency.reported";

    public static final String TRIP_EMERGENCY_DISMISSED_TO_USER =
        WebSocketTopic.PRIVATE_MESSAGE_PREFIX + "/trip.emergency.dismissed";

    public static final String TRIP_EMERGENCY_HANDLED_TO_USER =
        WebSocketTopic.PRIVATE_MESSAGE_PREFIX + "/trip.emergency.handled";
}
