package com.kernotec.driverschedule.service;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

public class ArchitectureTests {

    @Test
    void verifyArchitecture() {
        ApplicationModules modules = ApplicationModules.of("com.kernotec.driverschedule");

        new Documenter(modules).writeModulesAsPlantUml()
            .writeIndividualModulesAsPlantUml();

        // verify
        // modules.verify();
    }
}
