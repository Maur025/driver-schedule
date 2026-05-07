package com.kernotec.driverschedule.service.rest.dto.resource.response.person;

import java.util.UUID;

public interface PersonLookupResponse {

    UUID getId();

    String getName();

    String getLastName();
}
