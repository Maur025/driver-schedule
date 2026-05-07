package com.kernotec.driverschedule.person.socket;

import com.kernotec.driverschedule.socket.WebSocketTopic;

public class PersonSocketTopic {

    public static final String PERSON_CREATED = WebSocketTopic.BROKER_PREFIX + "/person.created";

    public static final String PERSON_UPDATED = WebSocketTopic.BROKER_PREFIX + "/person.updated";
}
