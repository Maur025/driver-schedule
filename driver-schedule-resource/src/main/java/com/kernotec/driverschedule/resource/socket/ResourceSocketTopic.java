package com.kernotec.driverschedule.resource.socket;

import com.kernotec.driverschedule.socket.WebSocketTopic;

public class ResourceSocketTopic {

    public static final String VEHICLE_CREATED = WebSocketTopic.BROKER_PREFIX + "/vehicle.created";

    public static final String VEHICLE_UPDATED = WebSocketTopic.BROKER_PREFIX + "/vehicle.updated";

    public static final String LOCATION_CREATED =
        WebSocketTopic.BROKER_PREFIX + "/location.created";

    public static final String LOCATION_UPDATED =
        WebSocketTopic.BROKER_PREFIX + "/location.updated";
}
