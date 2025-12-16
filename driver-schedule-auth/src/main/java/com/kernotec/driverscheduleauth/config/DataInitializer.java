package com.kernotec.driverscheduleauth.config;

import com.kernotec.driverscheduleauth.jpa.entity.Realm;
import com.kernotec.driverscheduleauth.jpa.entity.Role;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.entity.UserRole;
import com.kernotec.driverscheduleauth.jpa.service.RealmService;
import com.kernotec.driverscheduleauth.jpa.service.RoleService;
import com.kernotec.driverscheduleauth.jpa.service.UserRoleService;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
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
        PasswordEncoder passwordEncoder, UserService userService, RealmService realmService,
        RoleService roleService, UserRoleService userRoleService)
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

            Realm realm = realmOptional.get();

            Optional<Role> roleOptional = roleService.findByNameAndRealmIdAndResource(
                "ADMIN", realm.getId(), "driver-schedule");

            if (roleOptional.isEmpty()) {
                log.error(
                    "Admin role not found in realm 'driver-schedule-auth'. Cannot create admin user.");
                return;
            }

            User user = saveUserAdmin(
                passwordEncoder.encode(authConfigProperties.getAdminPassword()), realm.getId(),
                userService
            );

            saveRoleAdmin(
                user.getId(), roleOptional.get()
                    .getId(), userRoleService
            );
        };
    }

    public User saveUserAdmin(String password, UUID realmId, UserService userService) {
        var user = new User();
        user.setName("admin");
        user.setLastName("N/A");
        user.setUsername("admin");
        user.setPassword(password);
        user.setRealmId(realmId);
        user.setCreatedOn(ZonedDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy("system");
        user.setUpdatedAt(LocalDateTime.now());
        user.setVersion(0L);

        return userService.save(user);
    }

    public void saveRoleAdmin(UUID userId, UUID roleId, UserRoleService userRoleService) {
        var userRole = new UserRole();

        userRole.setRoleId(roleId);
        userRole.setUserId(userId);
        userRole.setCreatedAt(LocalDateTime.now());
        userRole.setUpdatedAt(LocalDateTime.now());
        userRole.setVersion(0L);

        userRoleService.save(userRole);
    }
}
