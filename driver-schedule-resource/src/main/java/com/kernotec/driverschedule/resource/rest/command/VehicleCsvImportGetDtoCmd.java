package com.kernotec.driverschedule.resource.rest.command;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.common.util.CsvImportUtil;
import com.kernotec.driverschedule.resource.jpa.enums.VehicleTypeEnum;
import com.kernotec.driverschedule.resource.rest.dto.VehicleCsvImportDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VehicleCsvImportGetDtoCmd extends
    AbstractCommand<VehicleCsvImportGetDtoCmd.Request, VehicleCsvImportDto>
{

    @Override
    protected VehicleCsvImportDto run(Request request) {
        String[] csvData = request.csvData;

        if (csvData == null || csvData.length == 0) {
            return null;
        }

        var vehicleExcelImportDto = new VehicleCsvImportDto();

        vehicleExcelImportDto.setVehicleNumber(CsvImportUtil.getValueOfCsv(csvData[1]));
        vehicleExcelImportDto.setModel(CsvImportUtil.getValueOfCsv(csvData[2]));
        vehicleExcelImportDto.setCapacity(CsvImportUtil.getIntegerValueOfCsv(csvData[3]));
        vehicleExcelImportDto.setVehicleType(
            getVehicleTypeEnum(CsvImportUtil.getValueOfCsv(csvData[4])));

        return vehicleExcelImportDto;
    }

    private VehicleTypeEnum getVehicleTypeEnum(String vehicleTypeStr) {
        return switch (vehicleTypeStr.toLowerCase()) {
            case "camioneta" -> VehicleTypeEnum.PICKUP;
            case "vagoneta" -> VehicleTypeEnum.WAGON;
            default -> null;
        };
    }

    @Builder
    public record Request(@NotNull String[] csvData) {

    }
}
