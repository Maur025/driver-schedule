package com.kernotec.driverscheduleservice.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reasons")
public class Reason extends BaseAuditEntity {

    @Column(name = "reason_description", nullable = false, length = 1500)
    private String reasonDescription;

    @ManyToMany(mappedBy = "rejectReasons")
    private Set<TransportationRequest> transportationRejectedRequests;
}
