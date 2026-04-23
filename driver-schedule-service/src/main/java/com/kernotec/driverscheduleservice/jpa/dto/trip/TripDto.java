package com.kernotec.driverscheduleservice.jpa.dto.trip;

import com.kernotec.driverscheduleservice.audit.user.dto.AuditEntityUserDto;
import com.kernotec.driverscheduleservice.jpa.dto.schedule.TripAssignmentDto;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDto extends AuditEntityUserDto {

    private ZonedDateTime tripStart;
    private ZonedDateTime tripEnd;
    private Double durationTotalMinutes;
    private Double onRouteTimeMinutes;
    private Double waitTimeMinutes;
    private String description;

    private UUID tripAssignmentId;
    private TripAssignmentDto tripAssignment;

    private UUID tripStateId;
    private TripStateDto tripState;
}
