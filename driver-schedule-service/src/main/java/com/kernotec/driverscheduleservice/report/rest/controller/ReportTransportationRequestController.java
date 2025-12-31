package com.kernotec.driverscheduleservice.report.rest.controller;

import com.kernotec.driverscheduleservice.rest.ApiSpec.ReportSpec;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = ReportSpec.TAG_NAME, description = ReportSpec.TAG_DESCRIPTION)
@RequestMapping(path = ReportSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class ReportTransportationRequestController {

    @Operation(summary = "export transportation request report to excel with filters")
    @PostMapping("transportation-requests/report/excel")
    @ResponseStatus(HttpStatus.OK)
    public void exportTrasportationRequestReportToExcel(HttpServletResponse response,
        @RequestParam(required = false) String titleReport,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") Boolean descending, Authentication authentication)
    {
    }
}
