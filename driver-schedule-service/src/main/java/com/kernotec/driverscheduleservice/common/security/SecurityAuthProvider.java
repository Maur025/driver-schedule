package com.kernotec.driverscheduleservice.common.security;

import com.kernotec.driverscheduleservice.jpa.enums.PersonTypeEnum;
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
