package com.kernotec.driverscheduleauth.rest.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GrantPasswordCredentialsRequest {

    private String username;
    private String password;
}
