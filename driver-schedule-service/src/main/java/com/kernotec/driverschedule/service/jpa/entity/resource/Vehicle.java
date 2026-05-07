package com.kernotec.driverschedule.service.jpa.entity.resource;

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
@Table(name = "vehicles")
public class Vehicle extends BaseAuditEntityUser {

    @Column(name = "vehicle_number", nullable = false)
    private String vehicleNumber;

    @Column(name = "model")
    private String model;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "is_enabled", nullable = false, columnDefinition = "boolean default true")
    private boolean isEnabled;

    @Column(name = "vehicle_type_id", nullable = false)
    private UUID vehicleTypeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private VehicleType vehicleType;
}
