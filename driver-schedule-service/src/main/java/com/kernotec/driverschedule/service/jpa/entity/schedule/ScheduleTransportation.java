package com.kernotec.driverschedule.service.jpa.entity.schedule;

import com.kernotec.driverschedule.service.audit.user.BaseAuditEntityUser;
import com.kernotec.driverschedule.service.request.jpa.entity.TransportationRequest;
import com.kernotec.driverschedule.service.jpa.entity.resource.Person;
import com.kernotec.driverschedule.service.util.SafeZoneDateTimeConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;
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
    private ZonedDateTime scheduledDate;

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

    @OneToMany(mappedBy = "scheduleTransportation", fetch = FetchType.LAZY,
               cascade = CascadeType.ALL)
    private List<CancelReason> cancelReasons;

    @OneToMany(mappedBy = "scheduleTransportation", fetch = FetchType.LAZY,
               cascade = CascadeType.ALL)
    private List<RescheduleReason> rescheduleReasons;

    @OneToMany(mappedBy = "scheduleTransportation", fetch = FetchType.LAZY,
               cascade = CascadeType.ALL)
    private List<TripAssignment> tripAssignments;
}
