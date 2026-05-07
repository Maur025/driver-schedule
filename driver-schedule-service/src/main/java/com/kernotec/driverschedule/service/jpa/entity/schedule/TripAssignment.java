package com.kernotec.driverschedule.service.jpa.entity.schedule;

import com.kernotec.driverschedule.service.audit.user.BaseAuditEntityUser;
import com.kernotec.driverschedule.service.jpa.entity.resource.Person;
import com.kernotec.driverschedule.service.jpa.entity.resource.Vehicle;
import com.kernotec.driverschedule.service.jpa.entity.trip.Trip;
import com.kernotec.driverschedule.service.util.SafeZoneDateTimeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "trip_assignments")
public class TripAssignment extends BaseAuditEntityUser {

    @Column(name = "estimated_start_time")
    @Convert(converter = SafeZoneDateTimeConverter.class)
    private ZonedDateTime estimatedStartTime;

    @Column(name = "estimated_end_time")
    @Convert(converter = SafeZoneDateTimeConverter.class)
    private ZonedDateTime estimatedEndTime;

    @Column(name = "vehicle_id", nullable = false)
    private UUID vehicleId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Vehicle vehicle;

    @Column(name = "driver_id", nullable = false)
    private UUID driverId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Person driver;

    @Column(name = "schedule_transportation_id", nullable = false)
    private UUID scheduleTransportationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_transportation_id", referencedColumnName = "id",
                insertable = false, updatable = false)
    private ScheduleTransportation scheduleTransportation;

    @Column(name = "trip_assignment_state_id", nullable = false)
    private UUID tripAssignmentStateId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_assignment_state_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private TripAssignmentState tripAssignmentState;

    @OneToMany(mappedBy = "tripAssignment", fetch = FetchType.LAZY)
    private Set<Trip> trips;
}
