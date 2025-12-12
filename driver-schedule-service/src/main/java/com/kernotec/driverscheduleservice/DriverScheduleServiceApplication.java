package com.kernotec.driverscheduleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.kernotec.core", "com.kernotec.driverscheduleservice"})
public class DriverScheduleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverScheduleServiceApplication.class, args);
    }
}
