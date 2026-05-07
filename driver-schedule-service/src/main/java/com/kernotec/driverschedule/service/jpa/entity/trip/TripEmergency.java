package com.kernotec.driverschedule.service.jpa.entity.trip;

import com.kernotec.driverschedule.service.audit.user.BaseAuditEntityUser;
import com.kernotec.driverschedule.service.jpa.entity.resource.Person;
import com.kernotec.driverschedule.service.jpa.entity.schedule.ScheduleTransportation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "trip_emergencies")
public class TripEmergency extends BaseAuditEntityUser {

    @Column(name = "person_emergency_reported_id", nullable = false)
    private UUID personEmergencyReportedId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_emergency_reported_id", referencedColumnName = "id",
                insertable = false, updatable = false)
    private Person personEmergencyReported;

    @Column(name = "trip_id", nullable = false)
    private UUID tripId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Trip trip;

    @Column(name = "schedule_transportation_id", nullable = false)
    private UUID scheduleTransportationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_transportation_id", referencedColumnName = "id",
                insertable = false, updatable = false)
    private ScheduleTransportation scheduleTransportation;

    @Column(name = "trip_emergency_state_id", nullable = false)
    private UUID tripEmergencyStateId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_emergency_state_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private TripEmergencyState tripEmergencyState;

    @OneToMany(mappedBy = "tripEmergency", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EmergencyReason> emergencyReasons;

    @OneToMany(mappedBy = "tripEmergency", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EmergencyRejectReason> emergencyRejectReasons;

    @OneToMany(mappedBy = "tripEmergency", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EmergencyResponse> emergencyResponses;
}
