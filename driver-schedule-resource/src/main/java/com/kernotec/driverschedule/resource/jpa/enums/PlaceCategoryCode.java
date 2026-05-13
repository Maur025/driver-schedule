package com.kernotec.driverschedule.resource.jpa.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PlaceCategoryCode {
    OFFICE("Oficina"),
    BANK("Banco"),
    BASE("Base"),
    MARKET("Mercado"),
    HOSPITAL("Hospital"),
    RESTAURANT("Restaurante"),
    SCHOOL("Escuela"),
    HOME("Casa"),
    TOWN_HALL("Alcaldia"),
    POLICE_STATION("Comisaria"),
    EMBASSY("Embajada"),
    CLINIC("Clinica"),
    PHARMACY("Farmacia"),
    VET("Veterinaria"),
    STORE("Tienda"),
    SUPERMARKET("Supermercado"),
    HARDWARE_STORE("Ferreteria"),
    MECHANICAL_WORKSHOP("Taller Mecanico"),
    HOTEL("Hotel"),
    BUS_TERMINAL("Terminal"),
    AIRPORT("Aeropuerto"),
    GAS_STATION("Gasolinera"),
    PARKING("Estacionamiento"),
    CHURCH("Iglesia"),
    STADIUM("Estadio"),
    SQUARE("Plaza"),
    OTHER("Otro"),
    INSTITUTION("Institucion"),
    PUBLIC_ENTITY("Entidad publica"),
    CABLE_CAR("Teleferico"),
    CINEMA("Cine"),
    PARK("Parque");

    private final String valueEs;

    public static PlaceCategoryCode fromValue(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        for (PlaceCategoryCode entry : values()) {
            if (entry.toString()
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }

    public static PlaceCategoryCode getByValueEs(String valueEs) {
        if (valueEs == null || valueEs.isEmpty()) {
            return null;
        }

        for (PlaceCategoryCode entry : values()) {
            if (entry.getValueEs() == null || entry.getValueEs()
                .isBlank())
            {
                continue;
            }

            if (entry.getValueEs()
                .equalsIgnoreCase(valueEs))
            {
                return entry;
            }
        }

        return null;
    }
}
