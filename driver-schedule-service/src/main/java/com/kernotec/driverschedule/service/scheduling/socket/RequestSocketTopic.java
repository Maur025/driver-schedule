package com.kernotec.driverschedule.service.scheduling.socket;

import com.kernotec.driverschedule.socket.WebSocketTopic;

public class RequestSocketTopic {

    public static final String TRANSPORTATION_REQUEST_CREATED =
        WebSocketTopic.BROKER_PREFIX + "/transportation.request.created";

    public static final String TRANSPORTATION_REQUEST_REJECTED =
        WebSocketTopic.BROKER_PREFIX + "/transportation.request.rejected";

    public static final String TRANSPORTATION_REQUEST_REJECTED_TO_USER =
        WebSocketTopic.PRIVATE_MESSAGE_PREFIX + "/transportation.request.rejected";

    public static final String TRANSPORTATION_REQUEST_CANCELLED =
        WebSocketTopic.BROKER_PREFIX + "/transportation.request.cancelled";
}
