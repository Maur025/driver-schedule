package com.kernotec.driverscheduleservice.jpa.util;

import jakarta.persistence.Embeddable;

@Embeddable
public record Coordinate(Double lat, Double lng) {

}
