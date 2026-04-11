package com.kernotec.driverscheduleservice.jpa.enums.schedule;

import java.util.Map;
import java.util.Set;

public enum ScheduleTransportationStateEnum {
    SCHEDULED, CANCELLED, RESCHEDULED, IN_PROGRESS, FINALIZED;

    private static final Map<ScheduleTransportationStateEnum, Set<ScheduleTransportationStateEnum>> TRANSITIONS = Map.of(
        SCHEDULED, Set.of(CANCELLED, RESCHEDULED, IN_PROGRESS),
        CANCELLED, Set.of(),
        RESCHEDULED, Set.of(RESCHEDULED, IN_PROGRESS, CANCELLED),
        IN_PROGRESS, Set.of(IN_PROGRESS, FINALIZED),
        FINALIZED, Set.of()
    );

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

    public boolean canTransitionTo(ScheduleTransportationStateEnum nextState) {
        if (nextState == null) {
            return false;
        }

        Set<ScheduleTransportationStateEnum> possibleNextStates = TRANSITIONS.get(this);

        if (possibleNextStates == null) {
            return false;
        }

        return possibleNextStates.contains(nextState);
    }
}
