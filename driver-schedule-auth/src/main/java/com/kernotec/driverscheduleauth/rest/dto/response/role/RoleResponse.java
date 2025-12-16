package com.kernotec.driverscheduleauth.rest.dto.response.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverscheduleauth.rest.dto.response.realm.RealmResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class RoleResponse extends EntityResponse {

    private String name;
    private String resource;

    private UUID realmId;
    private RealmResponse realm;
}
