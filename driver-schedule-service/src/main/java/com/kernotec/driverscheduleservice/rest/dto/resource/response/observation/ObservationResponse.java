package com.kernotec.driverscheduleservice.rest.dto.resource.response.observation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.observation.type.ObservationTypeResponse;
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
