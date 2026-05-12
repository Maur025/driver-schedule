package com.kernotec.driverschedule.service.schedule.jpa.entity;

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
@Table(name = "schedule_transportation_logs")
public class ScheduleTransportationLog extends BaseAuditEntityUser {

    @Column(name = "schedule_transportation_id", nullable = false)
    private UUID scheduleTransportationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_transportation_id", referencedColumnName = "id",
                insertable = false, updatable = false)
    private ScheduleTransportation scheduleTransportation;

    @Column(name = "schedule_transportation_state_id", nullable = false)
    private UUID scheduleTransportationStateId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_transportation_state_id", referencedColumnName = "id",
                insertable = false, updatable = false)
    private ScheduleTransportationState scheduleTransportationState;
}
