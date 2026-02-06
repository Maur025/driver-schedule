package com.kernotec.driverscheduleservice.rest.command.transportation.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.util.ResourceUtil;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import javax.sql.DataSource;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperRunManager;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoucherTransportationRequestPdfExportCmd extends
    AbstractTransactionalRequiredCommand<VoucherTransportationRequestPdfExportCmd.Request, byte[]>
{

    private final DataSource dataSource;

    @Override
    protected byte[] run(Request request) {

        try (Connection connection = dataSource.getConnection()) {
            String jasperFilePath = ResourceUtil.getAbsolutePath(
                "MyReports/transportation_request_voucher.jasper");

            return JasperRunManager.runReportToPdf(jasperFilePath, getParams(request), connection);
        } catch (SQLException | IOException | JRException ex) {
            log.error(
                "Error while exporting transportation request PDF for transportation request with id {} and zone id {}",
                request.transportationRequestId(), request.zoneId(), ex
            );

            throw new RuntimeException(ex);
        }
    }

    private Map<String, Object> getParams(Request request) {
        Map<String, Object> params = new HashMap<>();

        params.put(JRParameter.REPORT_TIME_ZONE, TimeZone.getTimeZone("UTC"));
        params.put("TRANSPORTATION_REQUEST_ID", request.transportationRequestId);
        params.put("ZONE_ID", request.zoneId);

        return params;
    }

    @Builder
    public record Request(@NotNull UUID transportationRequestId, @NotNull String zoneId) {

    }
}
