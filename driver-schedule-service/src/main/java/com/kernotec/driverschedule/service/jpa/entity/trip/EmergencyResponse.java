package com.kernotec.driverschedule.service.jpa.entity.trip;

import com.kernotec.driverschedule.common.audit.user.BaseAuditEntityUser;
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
@Table(name = "emergency_responses")
public class EmergencyResponse extends BaseAuditEntityUser {

    @Column(name = "detail", length = 1024)
    private String detail;

    @Column(name = "trip_emergency_id", nullable = false)
    private UUID tripEmergencyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_emergency_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private TripEmergency tripEmergency;

    @Column(name = "emergency_response_type_id", nullable = false)
    private UUID emergencyResponseTypeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emergency_response_type_id", referencedColumnName = "id",
                insertable = false, updatable = false)
    private EmergencyResponseType emergencyResponseType;
}
