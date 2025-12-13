package com.kernotec.driverscheduleservice.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.driverscheduleservice.jpa.util.Coordinate;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "locations")
public class Location extends BaseAuditEntity {

    @Embedded
    private Coordinate coordinate;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;
}
