package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.driverscheduleservice.rest.ApiSpec.TripSpec;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = TripSpec.TAG_NAME, description = TripSpec.TAG_DESCRIPTION)
@RequestMapping(path = TripSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class TripController {

}
