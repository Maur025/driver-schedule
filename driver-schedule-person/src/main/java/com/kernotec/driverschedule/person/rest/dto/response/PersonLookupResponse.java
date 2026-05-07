package com.kernotec.driverschedule.person.rest.dto.response;

import java.util.UUID;

public interface PersonLookupResponse {

    UUID getId();

    String getName();

    String getLastName();
}
