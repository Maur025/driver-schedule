package com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip;

import java.util.UUID;

public interface EmergencyResponseTypeLookupResponse {

    UUID getId();

    String getName();

    String getCode();
}
