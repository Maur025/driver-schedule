package com.kernotec.driverschedule.service.common.dto;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Coordinate {

    private Double lat;
    private Double lng;
}
