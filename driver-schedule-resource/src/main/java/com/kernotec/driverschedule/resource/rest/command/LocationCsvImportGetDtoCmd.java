package com.kernotec.driverschedule.resource.rest.command;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.common.util.CsvImportUtil;
import com.kernotec.driverschedule.resource.jpa.enums.PlaceCategoryCode;
import com.kernotec.driverschedule.resource.rest.dto.LocationCsvImportDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LocationCsvImportGetDtoCmd extends
    AbstractCommand<LocationCsvImportGetDtoCmd.Request, LocationCsvImportDto>
{

    @Override
    protected LocationCsvImportDto run(Request request) {
        String[] csvData = request.csvData();

        if (csvData == null || csvData.length == 0) {
            return null;
        }

        var locationCsvImportDto = new LocationCsvImportDto();

        // csvData[0] equal to number -> N° | ignoring

        locationCsvImportDto.setName(CsvImportUtil.getValueOfCsv(csvData[1]));
        locationCsvImportDto.setPlaceCategoryCode(
            PlaceCategoryCode.getByValueEs(CsvImportUtil.getValueOfCsv(csvData[2])));
        locationCsvImportDto.setLat(CsvImportUtil.getDoubleValueOfCsv(csvData[3]));
        locationCsvImportDto.setLng(CsvImportUtil.getDoubleValueOfCsv(csvData[4]));
        locationCsvImportDto.setDescription(CsvImportUtil.getValueOfCsv(csvData[5]));

        return locationCsvImportDto;
    }

    @Builder
    public record Request(@NotNull String[] csvData) {

    }
}