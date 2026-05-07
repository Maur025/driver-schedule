package com.kernotec.driverschedule.service.rest.dto.trip.response.emergency.response.type;

import java.util.UUID;

public interface EmergencyResponseTypeLookupResponse {

    UUID getId();

    String getName();

    String getCode();
}
