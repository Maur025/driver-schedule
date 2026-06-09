package com.kernotec.driverschedule.service.scheduling.rest.dto.request.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverschedule.common.dto.FilterDateRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ReportScheduleTransportationRequest extends FilterDateRequest {


}
