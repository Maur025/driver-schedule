package com.kernotec.driverschedule.report.service;

import com.kernotec.driverschedule.common.properties.KernotecApiProperties;
import com.kernotec.driverschedule.common.rest.ApiSpec;
import com.kernotec.driverschedule.report.enums.ReportDispositionEnum;
import com.kernotec.driverschedule.report.enums.VoucherParam;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRParameter;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportService {

    private final KernotecApiProperties kernotecApiProperties;
    private final ResourceLoader resourceLoader;

    public String getReportUrl(String resource, Object... uriVariables) {
        String baseUrlStr = kernotecApiProperties.getServers()
            .get(0)
            .getUrl();

        URI baseUri = URI.create(baseUrlStr);

        return UriComponentsBuilder.fromUri(baseUri)
            .path(ApiSpec.ROOT_PATH)
            .path(resource)
            .queryParam("disposition", ReportDispositionEnum.inline)
            .buildAndExpand(uriVariables)
            .toUriString();
    }

    public byte[] imageToByteArray(String imgResourcePath) {
        String path = "classpath:" + imgResourcePath;

        try {
            return resourceLoader.getResource(path)
                .getContentAsByteArray();
        } catch (IOException ex) {
            log.error("Error reading image from resource: {}", path, ex);
            throw new RuntimeException(ex);
        }
    }

    public Map<String, Object> getCommonParams(String zoneId) {
        Map<String, Object> params = new HashMap<>();

        params.put(JRParameter.REPORT_TIME_ZONE, TimeZone.getTimeZone("UTC"));
        params.put(
            VoucherParam.TITLE_LOGO_IMG.toString(),
            imageToByteArray(VoucherParam.TITLE_LOGO_IMG.getResourcePath())
        );
        params.put(VoucherParam.ZONE_ID.toString(), zoneId);

        return params;
    }
}
