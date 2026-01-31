package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.driverscheduleservice.rest.ApiSpec.TripStateSpec;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = TripStateSpec.TAG_NAME, description = TripStateSpec.TAG_DESCRIPTION)
@RequestMapping(path = TripStateSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class TripStateController {

}
