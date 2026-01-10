package com.kernotec.driverscheduleservice.rest.command.csv.imports;

import com.kernotec.core.command.AbstractCommand;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class CsvImportCmd<DTO> extends AbstractCommand<CsvImportCmd.Request<DTO>, Void> {

    private static final Integer BATCH_SIZE = 50;

    @Override
    protected Void run(Request<DTO> request) {
        int linesToSkip = Objects.requireNonNullElse(request.skipLines, 1);

        try (CSVReader reader = new CSVReaderBuilder(
            new InputStreamReader(request.excelFile.getInputStream())).withSkipLines(linesToSkip)
            .build())
        {
            String[] csvRow;
            List<DTO> dtoList = new ArrayList<>();

            while ((csvRow = reader.readNext()) != null) {
                DTO dto = request.mapperCallback.apply(csvRow);
                dtoList.add(dto);

                if (dtoList.size() >= BATCH_SIZE) {
                    processBatch(dtoList, request.saveCallback);
                }
            }

            if (!dtoList.isEmpty()) {
                processBatch(dtoList, request.saveCallback);
            }
        } catch (CsvValidationException | IOException ex) {
            log.error("Error reading Excel file", ex);
            throw new RuntimeException(ex);
        }

        return null;
    }

    private void processBatch(List<DTO> dtoList, Consumer<List<DTO>> saveCallback) {
        saveCallback.accept(new ArrayList<>(dtoList));
        log.info("Processed a batch of {} records", dtoList.size());
        dtoList.clear();
    }

    @Builder
    @Getter
    public static class Request<DTO> {

        @NotNull
        private final Function<String[], DTO> mapperCallback;
        @NotNull
        private final Consumer<List<DTO>> saveCallback;
        @NotNull
        private final MultipartFile excelFile;
        private final Integer skipLines;
    }
}
