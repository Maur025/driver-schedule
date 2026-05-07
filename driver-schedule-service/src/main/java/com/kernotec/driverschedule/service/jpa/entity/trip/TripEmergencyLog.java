package com.kernotec.driverschedule.service.jpa.entity.trip;

import com.kernotec.driverschedule.service.audit.user.BaseAuditEntityUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "trip_emergency_logs")
public class TripEmergencyLog extends BaseAuditEntityUser {

    @Column(name = "trip_emergency_id", nullable = false)
    private UUID tripEmergencyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_emergency_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private TripEmergency tripEmergency;

    @Column(name = "trip_emergency_state_id", nullable = false)
    private UUID tripEmergencyStateId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_emergency_state_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private TripEmergencyState tripEmergencyState;
}
