package com.kernotec.driverscheduleauth.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

public class RoleUtil {

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasRole('ADMIN')")
    public @interface IsRoleAdmin {

    }
}
