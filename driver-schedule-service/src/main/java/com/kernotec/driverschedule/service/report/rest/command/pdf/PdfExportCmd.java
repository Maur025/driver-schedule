package com.kernotec.driverschedule.service.report.rest.command.pdf;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.service.report.jpa.enums.ReportDispositionEnum;
import jakarta.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PdfExportCmd extends AbstractCommand<PdfExportCmd.Request, ResponseEntity<byte[]>> {

    @Override
    protected ResponseEntity<byte[]> run(Request request) {
        String fileName = Objects.requireNonNullElse(request.fileName, "report")
            .concat(".pdf");

        byte[] reportPdfBytes = request.callbackGetReportBytes.get();

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(
                HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.builder(
                        String.valueOf(request.disposition))
                    .filename(fileName, StandardCharsets.UTF_8)
                    .build()
                    .toString()
            )
            .contentLength(reportPdfBytes.length)
            .body(reportPdfBytes);
    }

    @Builder
    public record Request(@NotNull Supplier<byte[]> callbackGetReportBytes,
                          @NotNull ReportDispositionEnum disposition, String fileName)
    {

    }
}
