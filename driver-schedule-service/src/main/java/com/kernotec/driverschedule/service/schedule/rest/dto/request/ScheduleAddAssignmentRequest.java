package com.kernotec.driverschedule.service.schedule.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ScheduleAddAssignmentRequest extends BaseRequest {

    @NotNull
    private ZonedDateTime assignFrom;

    @NotNull
    private ZonedDateTime assignTo;

    @NotNull
    private String zoneId;

    @NotNull
    @NotEmpty
    private List<TripAssignmentCreateRequest> tripAssignments;
}
