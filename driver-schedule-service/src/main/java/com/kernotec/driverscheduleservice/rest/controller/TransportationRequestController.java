package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.driverscheduleservice.rest.ApiSpec.TransportationRequestSpec;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = TransportationRequestSpec.TAG_NAME,
     description = TransportationRequestSpec.TAG_DESCRIPTION)
@RequestMapping(path = TransportationRequestSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class TransportationRequestController {

}
