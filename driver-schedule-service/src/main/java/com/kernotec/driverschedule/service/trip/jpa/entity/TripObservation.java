package com.kernotec.driverschedule.service.trip.jpa.entity;

import com.kernotec.driverschedule.common.audit.user.BaseAuditEntityUser;
import com.kernotec.driverschedule.resource.jpa.entity.Observation;
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
@Table(name = "trip_observations")
public class TripObservation extends BaseAuditEntityUser {

    @Column(name = "other_observation")
    private String otherObservation;

    @Column(name = "trip_id", nullable = false)
    private UUID tripId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Trip trip;

    @Column(name = "observation_id", nullable = false)
    private UUID observationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "observation_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Observation observation;
}
