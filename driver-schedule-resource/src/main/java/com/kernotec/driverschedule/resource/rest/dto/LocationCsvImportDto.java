package com.kernotec.driverschedule.resource.rest.dto;

import com.kernotec.driverschedule.resource.jpa.enums.PlaceCategoryCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationCsvImportDto {

    private String name;
    private String description;
    private PlaceCategoryCode placeCategoryCode;
    private Double lat;
    private Double lng;
    private String icon;
    private String color;
}
