package com.kernotec.driverschedule.service.resource.rest.dto.response;

import java.util.UUID;

public interface WithReason {
    UUID getReasonId();

    void setReason(ReasonResponse reason);
}
