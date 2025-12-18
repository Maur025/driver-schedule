package com.kernotec.driverscheduleauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.kernotec.core", "com.kernotec.driverscheduleauth"})
public class DriverScheduleAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverScheduleAuthApplication.class, args);
    }

}
