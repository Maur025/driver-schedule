package com.kernotec.driverscheduleauth.config;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class JpaConfigAux {

    @Bean("auditorAware")
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("driver-schedule-auth-admin");
    }
}
