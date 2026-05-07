package com.kernotec.driverschedule.service.request.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.util.ResourceUtil;
import com.kernotec.driverschedule.service.util.VoucherJasperUtil;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoucherTransportationRequestPdfExportCmd extends
    AbstractTransactionalRequiredCommand<VoucherTransportationRequestPdfExportCmd.Request, byte[]>
{

    private final DataSource dataSource;
    private final VoucherJasperUtil voucherJasperUtil;

    @Override
    protected byte[] run(Request request) {

        Map<String, Object> params = voucherJasperUtil.getCommonParams(request.zoneId);
        params.put("TRANSPORTATION_REQUEST_ID", request.transportationRequestId.toString());

        try (Connection connection = dataSource.getConnection()) {
            String jasperFilePath = ResourceUtil.getAbsolutePath(
                "MyReports/transportation_request_voucher.jasper");

            return JasperRunManager.runReportToPdf(jasperFilePath, params, connection);
        } catch (SQLException | IOException | JRException ex) {
            log.error(
                "Error while exporting transportation request12 PDF for transportation request12 with id {} and zone id {}",
                request.transportationRequestId(), request.zoneId(), ex
            );

            throw new RuntimeException(ex);
        }
    }

    @Builder
    public record Request(@NotNull UUID transportationRequestId, @NotNull String zoneId) {

    }
}
