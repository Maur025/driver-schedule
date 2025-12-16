package com.kernotec.driverscheduleauth.rest.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverscheduleauth.rest.dto.response.realm.RealmResponse;
import com.kernotec.driverscheduleauth.rest.dto.response.role.RoleResponse;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class UserResponse extends EntityResponse {

    private String name;
    private String lastName;
    private String username;
    private ZonedDateTime createdOn;

    private UUID realmId;
    private RealmResponse realm;

    private Set<RoleResponse> roles;
}
