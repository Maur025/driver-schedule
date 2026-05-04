package com.kernotec.driverscheduleservice.jpa.enums.trip;

import java.util.Map;
import java.util.Set;

public enum TripStateEnum {
    PENDING, ON_ROUTE, WAITING, FINALIZED, EMERGENCY, SYSTEM_CLOSED;

    private static final Map<TripStateEnum, Set<TripStateEnum>> TRANSITIONS = Map.of(
        PENDING, Set.of(),
        ON_ROUTE, Set.of(ON_ROUTE, WAITING, FINALIZED, EMERGENCY, SYSTEM_CLOSED),
        WAITING, Set.of(WAITING, ON_ROUTE, FINALIZED, EMERGENCY, SYSTEM_CLOSED),
        FINALIZED, Set.of(),
        EMERGENCY, Set.of(WAITING, FINALIZED, SYSTEM_CLOSED),
        SYSTEM_CLOSED, Set.of()
    );

    public static TripStateEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (TripStateEnum entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }

    public boolean canTransitionTo(TripStateEnum nextState) {
        if (nextState == null) {
            return false;
        }

        Set<TripStateEnum> possibleNextStates = TRANSITIONS.get(this);

        if (possibleNextStates == null) {
            return false;
        }

        return possibleNextStates.contains(nextState);
    }
}
