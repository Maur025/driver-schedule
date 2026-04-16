package com.kernotec.driverscheduleservice.jpa.enums.schedule;

import java.util.Map;
import java.util.Set;

public enum ScheduleTransportationStateEnum {
    SCHEDULED, CANCELLED, RESCHEDULED, IN_PROGRESS, FINALIZED, NEEDS_ACTION;

    private static final Map<ScheduleTransportationStateEnum, Set<ScheduleTransportationStateEnum>> TRANSITIONS = Map.of(
        SCHEDULED, Set.of(CANCELLED, RESCHEDULED, IN_PROGRESS, NEEDS_ACTION),
        CANCELLED, Set.of(),
        RESCHEDULED, Set.of(RESCHEDULED, IN_PROGRESS, CANCELLED, NEEDS_ACTION),
        IN_PROGRESS, Set.of(IN_PROGRESS, FINALIZED),
        FINALIZED, Set.of(),
        NEEDS_ACTION, Set.of(RESCHEDULED, CANCELLED)
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
