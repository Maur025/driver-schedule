package com.kernotec.driverscheduleauth.config;

import com.kernotec.driverscheduleauth.jpa.entity.Realm;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.service.RealmService;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(AuthConfigProperties authConfigProperties,
        PasswordEncoder passwordEncoder, UserService userService, RealmService realmService)
    {
        return args -> {
            Optional<User> userOptional = userService.findByUsername("admin");

            if (userOptional.isPresent()) {
                log.warn("Admin user already exists. Skipping creation.");
                return;
            }

            Optional<Realm> realmOptional = realmService.findByName("driver-schedule-auth");

            if (realmOptional.isEmpty()) {
                log.error("Realm 'driver-schedule-auth' not found. Cannot create admin user.");
                return;
            }

            var user = new User();
            user.setName("admin");
            user.setLastName("N/A");
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode(authConfigProperties.getAdminPassword()));
            user.setRealmId(realmOptional.get()
                .getId());
            user.setCreatedOn(ZonedDateTime.now());
            user.setCreatedAt(LocalDateTime.now());
            user.setCreatedBy("system");
            user.setUpdatedAt(LocalDateTime.now());
            user.setVersion(0L);

            userService.save(user);
        };
    }
}
