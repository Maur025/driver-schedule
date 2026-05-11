package com.kernotec.driverschedule.service.schedule.jpa.dto;

import com.kernotec.core.jpa.dto.EntityDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleTransportationStateDto extends EntityDto {

    private String name;
    private String code;
}
