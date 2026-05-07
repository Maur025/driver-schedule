package com.kernotec.driverschedule.service.common.security;

import com.kernotec.driverschedule.service.jpa.enums.resource.PersonTypeEnum;
import java.util.Collection;
import java.util.UUID;

public interface SecurityAuthProvider {

    UUID getUserId();

    Collection<String> getRoles();

    boolean userContainsRole(PersonTypeEnum role);

    Collection<String> getScopes();

    boolean hasScope(String scope);

    boolean hasOnlyOneRole();
}
