package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.driverscheduleservice.rest.ApiSpec.ScheduleTransportationSpec;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = ScheduleTransportationSpec.TAG_NAME,
     description = ScheduleTransportationSpec.TAG_DESCRIPTION)
@RequestMapping(path = ScheduleTransportationSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class ScheduleTransportationController {

}
