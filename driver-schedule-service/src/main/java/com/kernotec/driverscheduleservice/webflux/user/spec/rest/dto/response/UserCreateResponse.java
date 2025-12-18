package com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.response;

import com.kernotec.core.rest.dto.response.data.EntityResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserCreateResponse extends EntityResponse {

    private String username;
}
