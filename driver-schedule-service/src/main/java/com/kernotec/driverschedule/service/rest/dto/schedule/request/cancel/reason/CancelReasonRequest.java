package com.kernotec.driverschedule.service.rest.dto.schedule.request.cancel.reason;

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
public class CancelReasonRequest extends BaseRequest {

    @NotNull
    private UUID reasonId;
    private String otherReason;
    @NotNull
    private UUID scheduleTransportationId;
}
