package com.kernotec.driverschedule.service.trip.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.driverschedule.resource.jpa.entity.Reason;
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
@Table(name = "emergency_reasons")
public class EmergencyReason extends BaseAuditEntity {

    @Column(name = "other_reason", length = 1500)
    private String otherReason;

    @Column(name = "reason_id", nullable = false)
    private UUID reasonId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reason_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Reason reason;

    @Column(name = "trip_emergency_id", nullable = false)
    private UUID tripEmergencyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_emergency_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private TripEmergency tripEmergency;
}
