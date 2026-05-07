package com.kernotec.driverschedule.service.rest.dto.resource.response.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverschedule.common.audit.user.dto.response.AuditEntityUserResponse;
import com.kernotec.driverschedule.service.rest.dto.resource.response.vehicle.type.VehicleTypeResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class VehicleResponse extends AuditEntityUserResponse {

    private String vehicleNumber;
    private String model;
    private Integer capacity;
    private boolean isEnabled;

    private UUID vehicleTypeId;
    private VehicleTypeResponse vehicleType;
}
