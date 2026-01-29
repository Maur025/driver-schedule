package com.kernotec.driverscheduleservice.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.driverscheduleservice.jpa.util.Coordinate;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@Table(name = "request_coords")
public class RequestCoord extends BaseAuditEntity {

    @Embedded
    private Coordinate coordinate;

    @Column(name = "index", nullable = false)
    private Integer index;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "locationName")
    private String locationName;

    @Column(name = "durationMinutes")
    private Double durationMinutes;

    @Column(name = "distanceKilometers")
    private Double distanceKilometers;

    @Column(name = "transportation_request_id", nullable = false)
    private UUID transportationRequestId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transportation_request_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private TransportationRequest transportationRequest;

    @Column(name = "location_id")
    private UUID locationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Location location;
}
