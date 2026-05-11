package com.kernotec.driverschedule.service.resource.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.resource.command.VehicleCreateManyCmd;
import com.kernotec.driverschedule.service.resource.jpa.entity.Vehicle;
import com.kernotec.driverschedule.service.resource.jpa.entity.VehicleType;
import com.kernotec.driverschedule.service.resource.jpa.enums.VehicleTypeEnum;
import com.kernotec.driverschedule.service.resource.jpa.service.VehicleTypeService;
import com.kernotec.driverschedule.service.resource.rest.dto.VehicleCsvImportDto;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class VehicleCsvImportSaveCmd extends
    AbstractTransactionalRequiredCommand<VehicleCsvImportSaveCmd.Request, Void>
{

    private final VehicleTypeService vehicleTypeService;
    private final VehicleCreateManyCmd vehicleCreateManyCmd;

    @Override
    protected Void run(Request request) {
        if (request.vehicleCsvImportDtoList.isEmpty()) {
            log.debug("No vehicle data to import.");
            return null;
        }

        Map<VehicleTypeEnum, VehicleType> vehicleTypeMap = vehicleTypeService.findAll()
            .stream()
            .collect(Collectors.toMap(
                vehicleType -> VehicleTypeEnum.fromValue(vehicleType.getCode()),
                vehicleType -> vehicleType
            ));

        List<Vehicle> vehicleToSaveList = new ArrayList<>();

        for (VehicleCsvImportDto vehicleCsvImportDto : request.vehicleCsvImportDtoList) {
            var vehicle = new Vehicle();

            VehicleType vehicleType = vehicleTypeMap.get(vehicleCsvImportDto.getVehicleType());

            vehicle.setVehicleNumber(vehicleCsvImportDto.getVehicleNumber());
            vehicle.setModel(vehicleCsvImportDto.getModel());
            vehicle.setCapacity(vehicleCsvImportDto.getCapacity());
            vehicle.setVehicleTypeId(vehicleType != null ? vehicleType.getId() : null);

            vehicleToSaveList.add(vehicle);
        }

        vehicleCreateManyCmd.withRequest(VehicleCreateManyCmd.Request.builder()
                .vehicleList(vehicleToSaveList)
                .build())
            .execute();

        return null;
    }

    @Builder
    public record Request(@NotNull List<VehicleCsvImportDto> vehicleCsvImportDtoList) {

    }
}
