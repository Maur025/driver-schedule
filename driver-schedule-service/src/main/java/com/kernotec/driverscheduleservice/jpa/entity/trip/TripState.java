package com.kernotec.driverscheduleservice.jpa.entity.trip;

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
@Table(name = "trip_states")
public class TripState extends BaseAuditEntity {

    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @Column(name = "code", nullable = false, length = 60, unique = true)
    private String code;
}
