package com.kernotec.driverscheduleservice.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.driverscheduleservice.jpa.enums.LocationTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OptimisticLock;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "request_locations")
public class RequestLocation extends BaseAuditEntity {

    @Column(name = "location_id", nullable = false)
    private UUID locationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Location location;

    @Column(name = "transportation_request_id", nullable = false)
    private UUID transportationRequestId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transportation_request_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private TransportationRequest transportationRequest;

    @OptimisticLock(excluded = true)
    @Enumerated(EnumType.STRING)
    @Column(name = "location_type", nullable = false)
    private LocationTypeEnum locationType;
}
