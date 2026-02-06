package com.kernotec.driverscheduleservice.rest.dto.response.person;

import java.util.UUID;

public interface PersonLookupResponse {

    UUID getId();

    String getName();

    String getLastName();
}
