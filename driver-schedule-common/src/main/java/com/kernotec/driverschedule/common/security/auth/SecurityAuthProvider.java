package com.kernotec.driverschedule.common.security.auth;

import java.util.Collection;
import java.util.UUID;

public interface SecurityAuthProvider {

    UUID getUserId();

    Collection<String> getRoles();

    boolean userContainsRole(UserRoleType role);

    Collection<String> getScopes();

    boolean hasScope(String scope);

    boolean hasOnlyOneRole();
}
