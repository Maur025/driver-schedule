package com.kernotec.driverschedule.service.rest.dto.resource.request.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@JsonInclude(Include.NON_NULL)
public class VehiclePatchRequest extends BaseRequest {

    private Boolean isEnabled;
}
