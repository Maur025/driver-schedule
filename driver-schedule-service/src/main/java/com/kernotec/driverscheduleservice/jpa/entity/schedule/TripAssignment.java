package com.kernotec.driverscheduleservice.jpa.entity.schedule;

import com.kernotec.driverscheduleservice.audit.user.BaseAuditEntityUser;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Person;
import com.kernotec.driverscheduleservice.jpa.entity.trip.Trip;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Vehicle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @OneToMany(mappedBy = "tripAssignment", fetch = FetchType.LAZY)
    private Set<Trip> trips;
}
