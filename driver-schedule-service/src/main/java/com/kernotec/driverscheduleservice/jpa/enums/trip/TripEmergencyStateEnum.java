package com.kernotec.driverscheduleservice.jpa.enums.trip;

import java.util.Map;
import java.util.Set;

public enum TripEmergencyStateEnum {
    REPORTED, ACKNOWLEDGED, HANDLED, DISMISSED;

    private static final Map<TripEmergencyStateEnum, Set<TripEmergencyStateEnum>> TRANSITIONS = Map.of(
        REPORTED, Set.of(ACKNOWLEDGED),
        ACKNOWLEDGED, Set.of(HANDLED, DISMISSED),
        HANDLED, Set.of(),
        DISMISSED, Set.of()
    );

    public static TripEmergencyStateEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (TripEmergencyStateEnum entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }

    public boolean canTransitionTo(TripEmergencyStateEnum nextState) {
        if (nextState == null) {
            return false;
        }

        Set<TripEmergencyStateEnum> possibleNextStates = TRANSITIONS.get(this);

        if (possibleNextStates == null) {
            return false;
        }

        return possibleNextStates.contains(nextState);
    }
}
