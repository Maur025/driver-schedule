package com.kernotec.driverschedule.service.request.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.driverschedule.resource.jpa.entity.Location;
import com.kernotec.driverschedule.common.dto.Coordinate;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

    @Column(name = "wait_time_minutes")
    private Double waitTimeMinutes;

    @Column(name = "estimated_arrival_time")
    private ZonedDateTime estimatedArrivalTime;

    @Column(name = "location_description", length = 512)
    private String locationDescription;

    @Column(name = "transportation_request_id", nullable = false)
    private UUID transportationRequestId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transportation_request_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private TransportationRequest transportationRequest;

    @Column(name = "location_id")
    private UUID locationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Location location;
}
