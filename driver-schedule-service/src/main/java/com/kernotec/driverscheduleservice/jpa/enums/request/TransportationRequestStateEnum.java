package com.kernotec.driverscheduleservice.jpa.enums.request;

import java.util.Map;
import java.util.Set;

public enum TransportationRequestStateEnum {
    REQUESTED, APPROVED, REJECTED, CANCELLED;

    private static final Map<TransportationRequestStateEnum, Set<TransportationRequestStateEnum>> TRANSITIONS = Map.of(
        REQUESTED, Set.of(APPROVED, REJECTED, CANCELLED),
        APPROVED, Set.of(),
        REJECTED, Set.of(),
        CANCELLED, Set.of()
    );

    public static TransportationRequestStateEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (TransportationRequestStateEnum entry : values()) {
            if (String.valueOf(entry)
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }

    public boolean canTransitionTo(TransportationRequestStateEnum nextState) {
        if (nextState == null) {
            return false;
        }

        Set<TransportationRequestStateEnum> possibleNextStates = TRANSITIONS.get(this);

        if (possibleNextStates == null) {
            return false;
        }

        return possibleNextStates.contains(nextState);
    }
}
