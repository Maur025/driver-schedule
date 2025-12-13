package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.driverscheduleservice.rest.ApiSpec.LocationSpec;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = LocationSpec.TAG_NAME, description = LocationSpec.TAG_DESCRIPTION)
@RequestMapping(path = LocationSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class LocationController {

}
