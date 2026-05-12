package com.kernotec.driverschedule.resource.rest.command;

import com.kernotec.core.command.AbstractCommand;
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

        if (csvData == null) {
            return null;
        }

        var vehicleExcelImportDto = new VehicleCsvImportDto();

        vehicleExcelImportDto.setVehicleNumber(csvData[0].isBlank() ? null : csvData[0]);
        vehicleExcelImportDto.setModel(csvData[1].isBlank() ? null : csvData[1]);
        vehicleExcelImportDto.setCapacity(
            csvData[2].isBlank() ? null : Integer.parseInt(csvData[2]));
        vehicleExcelImportDto.setVehicleType(
            csvData[3].isBlank() ? null : getVehicleTypeEnum(csvData[3]));

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
