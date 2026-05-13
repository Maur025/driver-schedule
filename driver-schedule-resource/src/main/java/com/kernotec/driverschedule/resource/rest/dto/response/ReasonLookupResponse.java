package com.kernotec.driverschedule.resource.rest.dto.response;

import java.util.UUID;

public interface ReasonLookupResponse {

    UUID getId();

    String getValue();

    String getCode();
}
