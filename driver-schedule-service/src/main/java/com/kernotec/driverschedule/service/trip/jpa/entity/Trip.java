package com.kernotec.driverschedule.service.trip.jpa.entity;

import com.kernotec.driverschedule.common.audit.user.BaseAuditEntityUser;
import com.kernotec.driverschedule.service.schedule.jpa.entity.TripAssignment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "trips")
public class Trip extends BaseAuditEntityUser {

    @Column(name = "trip_start")
    private ZonedDateTime tripStart;

    @Column(name = "trip_end")
    private ZonedDateTime tripEnd;

    @Column(name = "duration_total_minutes")
    private Double durationTotalMinutes;

    @Column(name = "on_route_time_minutes")
    private Double onRouteTimeMinutes;

    @Column(name = "wait_time_minutes")
    private Double waitTimeMinutes;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "trip_assignment_id", nullable = false)
    private UUID tripAssignmentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_assignment_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private TripAssignment tripAssignment;

    @Column(name = "trip_state_id", nullable = false)
    private UUID tripStateId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_state_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private TripState tripState;
}
