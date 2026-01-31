package com.kernotec.driverscheduleservice.rest.dto.response.reason;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.reason.type.ReasonTypeResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ReasonResponse extends EntityResponse {

    private String value;
    private String code;

    private UUID reasonTypeId;
    private ReasonTypeResponse reasonType;
}
