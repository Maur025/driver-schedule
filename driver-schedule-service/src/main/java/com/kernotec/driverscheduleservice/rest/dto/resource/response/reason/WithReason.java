package com.kernotec.driverscheduleservice.rest.dto.resource.response.reason;

import java.util.UUID;

public interface WithReason {
    UUID getReasonId();

    void setReason(ReasonResponse reason);
}
