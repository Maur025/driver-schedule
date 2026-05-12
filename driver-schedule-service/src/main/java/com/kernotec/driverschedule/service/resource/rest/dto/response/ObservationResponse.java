package com.kernotec.driverschedule.service.resource.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ObservationResponse extends EntityResponse {

    private String name;
    private String code;

    private UUID observationTypeId;
    private ObservationTypeResponse observationType;
}
