package com.kernotec.driverscheduleservice.report.rest.command.excel;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExcelExportCmd extends
    AbstractTransactionalRequiredCommand<ExcelExportCmd.Request, Void>
{

    private final static String MEDIA_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Override
    protected Void run(Request request) {
        HttpServletResponse response = request.response;

        try (var workbook = new SXSSFWorkbook(500); OutputStream out = response.getOutputStream()) {
            Sheet sheet = workbook.createSheet(
                request.reportTitle == null ? "Report" : request.reportTitle);

            request.callback.accept(sheet);

            response.setContentType(MEDIA_TYPE_XLSX);
            response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                    .filename(request.fileName(), StandardCharsets.UTF_8)
                    .build()
                    .toString()
            );

            workbook.write(out);
            out.flush();
        } catch (IOException ex) {
            log.error("Error exporting Excel report: {}", ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

        return null;
    }

    @Builder
    public record Request(@NotNull Consumer<Sheet> callback, @NotNull HttpServletResponse response,
                          String reportTitle, String fileName)
    {

    }
}
