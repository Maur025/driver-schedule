package com.kernotec.driverscheduleservice.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "vehicles")
public class Vehicle extends BaseAuditEntity {

    @Column(name = "vehicle_number", nullable = false)
    private String vehicleNumber;

    @Column(name = "model")
    private String model;

    @Column(name = "capacity")
    private Integer capacity;
}
