package com.kernotec.driverscheduleservice.jpa.entity.request;

import com.kernotec.driverscheduleservice.audit.user.BaseAuditEntityUser;
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
@Table(name = "transportation_request_logs")
public class TransportationRequestLog extends BaseAuditEntityUser {

    @Column(name = "transportation_request_id", nullable = false)
    private UUID transportationRequestId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transportation_request_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private TransportationRequest transportationRequest;

    @Column(name = "transportation_request_state_id", nullable = false)
    private UUID transportationRequestStateId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transportation_request_state_id", referencedColumnName = "id",
                insertable = false, updatable = false)
    private TransportationRequestState transportationRequestState;
}
