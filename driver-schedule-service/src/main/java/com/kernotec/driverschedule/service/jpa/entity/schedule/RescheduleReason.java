package com.kernotec.driverschedule.service.jpa.entity.schedule;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.driverschedule.service.jpa.entity.resource.Reason;
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
@Table(name = "reschedule_reasons")
public class RescheduleReason extends BaseAuditEntity {

    @Column(name = "other_reason", length = 1500)
    private String otherReason;

    @Column(name = "reason_id", nullable = false)
    private UUID reasonId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reason_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Reason reason;

    @Column(name = "schedule_transportation_id", nullable = false)
    private UUID scheduleTransportationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_transportation_id", referencedColumnName = "id",
                insertable = false, updatable = false)
    private ScheduleTransportation scheduleTransportation;
}
