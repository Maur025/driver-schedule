package com.kernotec.driverschedule.service.schedule.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@JsonInclude(Include.NON_NULL)
public class ScheduleTransportationCancelRequest extends BaseRequest {

    @NotNull
    private UUID reasonId;
    private String otherReason;
}
