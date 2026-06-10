package com.kernotec.driverschedule.service.scheduling.rest.command.schedule;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.common.service.VoucherPdfExportService;
import com.kernotec.driverschedule.service.scheduling.common.util.ResourceUtil;
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
public class VoucherScheduleTransportationPdfExportCmd extends
    AbstractTransactionalRequiredCommand<VoucherScheduleTransportationPdfExportCmd.Request, byte[]>
{

    private final DataSource dataSource;
    private final VoucherPdfExportService voucherPdfExportService;

    @Override
    protected byte[] run(Request request) {

        Map<String, Object> params = voucherPdfExportService.getVoucherParams(request.zoneId);
        params.put("SCHEDULE_TRANSPORTATION_ID", request.scheduleTransportationId.toString());

        try (Connection connection = dataSource.getConnection()) {
            String jasperFilePath = ResourceUtil.getAbsolutePath(
                "MyReports/schedule_transportation_voucher.jasper");

            return JasperRunManager.runReportToPdf(jasperFilePath, params, connection);
        } catch (SQLException | IOException | JRException ex) {
            log.error(
                "Error while exporting response transportation PDF for response transportation with id {} and zone id {}",
                request.scheduleTransportationId(), request.zoneId(), ex
            );

            throw new RuntimeException(ex);
        }
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId, @NotNull String zoneId) {

    }
}
