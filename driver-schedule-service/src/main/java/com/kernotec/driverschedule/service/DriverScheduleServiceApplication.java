package com.kernotec.driverschedule.service;

import java.time.ZoneOffset;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.kernotec.core", "com.kernotec.driverschedule"})
@EntityScan({"com.kernotec.driverschedule"})
@EnableJpaRepositories({"com.kernotec.driverschedule"})
public class DriverScheduleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverScheduleServiceApplication.class, args);
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
    }
}
