package com.kernotec.driverschedule.service.scheduling.socket;

import com.kernotec.driverschedule.socket.WebSocketTopic;

public class ScheduleSocketTopic {

    public static final String SCHEDULE_TRANSPORTATION_CREATED =
        WebSocketTopic.BROKER_PREFIX + "/schedule.transportation.created";

    public static final String SCHEDULE_TRANSPORTATION_CREATED_TO_USER =
        WebSocketTopic.PRIVATE_MESSAGE_PREFIX + "/schedule.transportation.created";

    public static final String SCHEDULE_TRANSPORTATION_RESCHEDULED =
        WebSocketTopic.BROKER_PREFIX + "/schedule.transportation.rescheduled";

    public static final String SCHEDULE_TRANSPORTATION_RESCHEDULED_TO_USER =
        WebSocketTopic.PRIVATE_MESSAGE_PREFIX + "/schedule.transportation.rescheduled";

    public static final String SCHEDULE_TRANSPORTATION_CANCELLED =
        WebSocketTopic.BROKER_PREFIX + "/schedule.transportation.cancelled";

    public static final String SCHEDULE_TRANSPORTATION_CANCELLED_TO_USER =
        WebSocketTopic.PRIVATE_MESSAGE_PREFIX + "/schedule.transportation.cancelled";

    public static final String SCHEDULE_TRANSPORTATION_ON_PROGRESS =
        WebSocketTopic.BROKER_PREFIX + "/schedule.transportation.on.progress";

    public static final String SCHEDULE_TRANSPORTATION_FINALIZED =
        WebSocketTopic.BROKER_PREFIX + "/schedule.transportation.finalized";
}
