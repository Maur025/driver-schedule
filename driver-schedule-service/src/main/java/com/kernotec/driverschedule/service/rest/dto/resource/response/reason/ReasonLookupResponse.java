package com.kernotec.driverschedule.service.rest.dto.resource.response.reason;

import java.util.UUID;

public interface ReasonLookupResponse {

    UUID getId();

    String getValue();

    String getCode();
}
