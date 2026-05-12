package com.kernotec.driverschedule.service.resource.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "observations",
       uniqueConstraints = @UniqueConstraint(name = "UC_observation_code_type",
                                             columnNames = {"code", "observation_type_id"}))
public class Observation extends BaseAuditEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "observation_type_id", nullable = false)
    private UUID observationTypeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "observation_type_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private ObservationType observationType;
}
