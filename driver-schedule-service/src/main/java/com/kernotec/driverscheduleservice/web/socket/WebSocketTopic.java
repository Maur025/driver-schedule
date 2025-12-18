package com.kernotec.driverscheduleservice.web.socket;

public class WebSocketTopic {

    public static final String BROKER_PREFIX = "/topic";
    public static final String APP_PREFIX = "/app";

    public static final String VEHICLE_CREATED = BROKER_PREFIX + "/vehicle.created";

    public static final String VEHICLE_UPDATED = BROKER_PREFIX + "/vehicle.updated";

    public static final String PERSON_CREATED = BROKER_PREFIX + "/person.created";

    public static final String PERSON_UPDATED = BROKER_PREFIX + "/person.updated";

    public static final String LOCATION_CREATED = BROKER_PREFIX + "/location.created";

    public static final String LOCATION_UPDATED = BROKER_PREFIX + "/location.updated";

    public static final String TEST_MESSAGE = "/test.message";

    private WebSocketTopic() {
    }
}
