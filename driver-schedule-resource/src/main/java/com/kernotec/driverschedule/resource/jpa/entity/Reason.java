package com.kernotec.driverschedule.resource.jpa.entity;

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
@Table(name = "reasons")
public class Reason extends BaseAuditEntity {

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "reason_type_id", nullable = false)
    private UUID reasonTypeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reason_type_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private ReasonType reasonType;
}
