package com.kernotec.driverscheduleservice.jpa.entity;

import com.kernotec.driverscheduleservice.audit.user.BaseAuditEntityUser;
import com.kernotec.driverscheduleservice.jpa.enums.TripTypeEnum;
import com.kernotec.driverscheduleservice.util.SafeZoneDateTimeConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.OptimisticLock;
import org.hibernate.generator.EventType;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "transportation_requests")
public class TransportationRequest extends BaseAuditEntityUser {

    @Column(name = "people_number", nullable = false)
    private String peopleNumber;

    @Column(name = "assets", length = 1000)
    private String assets;

    @Column(name = "passengers", length = 1000)
    private String passengers;

    @Column(name = "start_time", nullable = false)
    @Convert(converter = SafeZoneDateTimeConverter.class)
    private ZonedDateTime startTime;

    @Column(name = "end_time", nullable = false)
    @Convert(converter = SafeZoneDateTimeConverter.class)
    private ZonedDateTime endTime;

    @Column(name = "requested_date")
    private LocalDateTime requestedDate;

    @Column(name = "requested_from")
    @Convert(converter = SafeZoneDateTimeConverter.class)
    private ZonedDateTime requestedFrom;

    @Column(name = "requested_to")
    @Convert(converter = SafeZoneDateTimeConverter.class)
    private ZonedDateTime requestedTo;

    @OptimisticLock(excluded = true)
    @Enumerated(EnumType.STRING)
    @Column(name = "trip_type", nullable = false)
    private TripTypeEnum tripType;

    @Column(name = "is_short_notice", nullable = false, columnDefinition = "boolean default false")
    private boolean isShortNotice = false;

    @Generated(event = EventType.INSERT)
    @Column(name = "correlative", nullable = false, insertable = false, updatable = false)
    private Long correlative;

    @Column(name = "detail", length = 2048)
    private String detail;

    @Column(name = "is_asset_pickup", nullable = false, columnDefinition = "boolean default false")
    private boolean isAssetPickup = false;

    @Column(name = "estimated_total_distance_km")
    private Double estimatedTotalDistanceKm;

    @Column(name = "estimated_total_duration_min")
    private Double estimatedTotalDurationMin;

    @Column(name = "code", length = 30, unique = true)
    private String code;

    @Column(name = "was_requested_by_scheduler", nullable = false,
            columnDefinition = "boolean default false")
    private boolean wasRequestedByScheduler = false;

    @Column(name = "transportation_request_state_id", nullable = false)
    private UUID transportationRequestStateId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transportation_request_state_id", referencedColumnName = "id",
                insertable = false, updatable = false)
    private TransportationRequestState transportationRequestState;

    @Column(name = "person_requested_id")
    private UUID personRequestedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_requested_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Person personRequested;

    @OneToMany(mappedBy = "transportationRequest", fetch = FetchType.LAZY,
               cascade = CascadeType.ALL)
    private List<RejectReason> rejectReasons;

    @OneToMany(mappedBy = "transportationRequest", fetch = FetchType.LAZY,
               cascade = CascadeType.ALL)
    private List<CancelRequestReason> cancelReasons;

    @OneToMany(mappedBy = "transportationRequest", fetch = FetchType.LAZY)
    private Set<ScheduleTransportation> scheduleTransportations;

    @OneToMany(mappedBy = "transportationRequest", fetch = FetchType.LAZY,
               cascade = CascadeType.ALL)
    private List<RequestCoord> requestCoords;
}
