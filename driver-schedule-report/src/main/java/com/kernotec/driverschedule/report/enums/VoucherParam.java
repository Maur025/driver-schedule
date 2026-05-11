package com.kernotec.driverschedule.report.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VoucherParam {
    TITLE_LOGO_IMG("images/logo-kerno-booking.png"),
    ZONE_ID(null);

    private final String resourcePath;
}
