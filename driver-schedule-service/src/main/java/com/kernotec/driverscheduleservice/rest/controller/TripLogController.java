package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.driverscheduleservice.rest.ApiSpec.TripLogSpec;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = TripLogSpec.TAG_NAME, description = TripLogSpec.TAG_DESCRIPTION)
@RequestMapping(path = TripLogSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class TripLogController {

}
