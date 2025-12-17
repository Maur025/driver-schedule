package com.kernotec.driverscheduleservice.rest.dto.request.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
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
}
