package com.kernotec.driverschedule.service.resource.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class VehicleUpdateRequest extends BaseRequest {

    private String vehicleNumber;
    private String model;
    private Integer capacity;
    private UUID vehicleTypeId;
}
