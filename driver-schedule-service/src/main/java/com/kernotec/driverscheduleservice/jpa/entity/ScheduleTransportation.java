package com.kernotec.driverscheduleservice.jpa.entity;

import com.kernotec.driverscheduleservice.audit.user.BaseAuditEntityUser;
import com.kernotec.driverscheduleservice.util.SafeZoneDateTimeConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "schedule_transportations")
public class ScheduleTransportation extends BaseAuditEntityUser {

    @Column(name = "schedule_from", nullable = false)
    @Convert(converter = SafeZoneDateTimeConverter.class)
    private ZonedDateTime scheduleFrom;

    @Column(name = "schedule_to", nullable = false)
    @Convert(converter = SafeZoneDateTimeConverter.class)
    private ZonedDateTime scheduleTo;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;

    @Column(name = "transportation_request_id", nullable = false)
    private UUID transportationRequestId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transportation_request_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private TransportationRequest transportationRequest;

    @Column(name = "person_requested_id", nullable = false)
    private UUID personRequestedId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_requested_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Person personRequested;

    @Column(name = "schedule_transportation_state_id", nullable = false)
    private UUID scheduleTransportationStateId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_transportation_state_id", referencedColumnName = "id",
                insertable = false, updatable = false)
    private ScheduleTransportationState scheduleTransportationState;

    @ManyToMany
    @JoinTable(name = "cancel_reasons",
               joinColumns = @JoinColumn(name = "schedule_transportation_id",
                                         referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "reason_id", referencedColumnName = "id"))
    private Set<Reason> cancelReasons;

    @ManyToMany
    @JoinTable(name = "reschedule_reasons",
               joinColumns = @JoinColumn(name = "schedule_transportation_id",
                                         referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "reason_id", referencedColumnName = "id"))
    private Set<Reason> rescheduleReasons;

    @OneToMany(mappedBy = "scheduleTransportation", fetch = FetchType.LAZY,
               cascade = CascadeType.ALL)
    private List<TripAssignment> tripAssignments;
}
