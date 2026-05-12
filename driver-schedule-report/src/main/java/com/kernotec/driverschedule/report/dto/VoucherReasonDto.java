package com.kernotec.driverschedule.report.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoucherReasonDto {

    private String reasonLabel;
    private String reasonCode;
    private String otherReason;
}
