package com.kernotec.driverscheduleservice.util.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoucherReasonDto {

    private String reasonLabel;
    private String reasonCode;
    private String otherReason;
}
