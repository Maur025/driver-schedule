package com.kernotec.driverscheduleservice.util.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoucherContactDto {

    private UUID id;
    private String value;
    private String label;
}
