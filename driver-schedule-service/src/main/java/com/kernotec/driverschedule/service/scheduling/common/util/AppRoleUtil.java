package com.kernotec.driverschedule.service.scheduling.common.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

public class AppRoleUtil {

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAnyRole('ADMIN','SCHEDULER')")
    public @interface IsRoleSchedulerOrAdmin {

    }

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasRole('APPLICANT')")
    public @interface IsRoleApplicant {

    }

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAnyRole('APPLICANT','SCHEDULER')")
    public @interface IsRoleApplicantOrScheduler {

    }
}
