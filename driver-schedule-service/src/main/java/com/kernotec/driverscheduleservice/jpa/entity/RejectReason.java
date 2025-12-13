package com.kernotec.driverscheduleservice.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
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
@Table(name = "reject_reasons")
public class RejectReason extends BaseAuditEntity {

    @Column(name = "reason_id", nullable = false)
    private UUID reasonId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reason_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Reason reason;

    @Column(name = "trasportation_request_id", nullable = false)
    private UUID trasportationRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trasportation_request_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private TransportationRequest transportationRequest;
}
