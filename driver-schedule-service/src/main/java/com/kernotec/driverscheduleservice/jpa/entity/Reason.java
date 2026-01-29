package com.kernotec.driverscheduleservice.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reasons")
public class Reason extends BaseAuditEntity {

    @Column(name = "value")
    private String value;

    @Column(name = "reason_description", length = 1500)
    private String reasonDescription;

    @Column(name = "reason_type_id")
    private UUID reasonTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_type_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private ReasonType reasonType;

    @ManyToMany(mappedBy = "rejectReasons")
    private Set<TransportationRequest> transportationRejectedRequests;
}
