package com.kernotec.driverscheduleservice.jpa.entity;

import com.kernotec.driverscheduleservice.audit.user.BaseAuditEntityUser;
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
@Table(name = "trip_logs")
public class TripLog extends BaseAuditEntityUser {

    @Column(name = "trip_id", nullable = false)
    private UUID tripId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Trip trip;

    @Column(name = "trip_state_id", nullable = false)
    private UUID tripStateId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_state_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private TripState tripState;
}
