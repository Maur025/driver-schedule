package com.kernotec.driverscheduleservice.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.driverscheduleservice.jpa.enums.TripTypeEnum;
import com.kernotec.driverscheduleservice.jpa.util.Coordinate;
import com.kernotec.driverscheduleservice.util.SafeZoneDateTimeConverter;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OptimisticLock;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "transportation_requests")
public class TransportationRequest extends BaseAuditEntity {

    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "lat", column = @Column(name = "starting_lat",
                                                                           nullable = false)),
        @AttributeOverride(name = "lng",
                           column = @Column(name = "starting_lng", nullable = false))})
    private Coordinate startingCoordinate;

    @Embedded
    @AttributeOverrides(
        {@AttributeOverride(name = "lat", column = @Column(name = "end_lat", nullable = false)),
            @AttributeOverride(name = "lng", column = @Column(name = "end_lng", nullable = false))})
    private Coordinate endCoordinate;

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

    @OptimisticLock(excluded = true)
    @Enumerated(EnumType.STRING)
    @Column(name = "trip_type", nullable = false)
    private TripTypeEnum tripType;

    @Column(name = "is_short_notice", nullable = false, columnDefinition = "boolean default false")
    private boolean isShortNotice = false;

    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "transportation_request_correlative_seq")
    @SequenceGenerator(name = "transportation_request_correlative_seq",
                       sequenceName = "transportation_request_correlative_seq_db",
                       allocationSize = 1)
    @Column(name = "correlative", nullable = false, insertable = false, updatable = false)
    private Long correlative;

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

    @ManyToMany
    @JoinTable(name = "reject_reasons",
               joinColumns = @JoinColumn(name = "transportation_request_id",
                                         referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "reason_id", referencedColumnName = "id"))
    private Set<Reason> rejectReasons;

    @ManyToMany
    @JoinTable(name = "cancel_request_reasons",
               joinColumns = @JoinColumn(name = "transportation_request_id",
                                         referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "reason_id", referencedColumnName = "id"))
    private Set<Reason> cancelReasons;

    @OneToMany(mappedBy = "transportationRequest", fetch = FetchType.LAZY)
    private List<RequestLocation> requestLocations;
}
