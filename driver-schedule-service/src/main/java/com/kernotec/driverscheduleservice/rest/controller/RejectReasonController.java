package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.driverscheduleservice.rest.ApiSpec.RejectReasonSpec;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = RejectReasonSpec.TAG_NAME, description = RejectReasonSpec.TAG_DESCRIPTION)
@RequestMapping(path = RejectReasonSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class RejectReasonController {

}
