package com.kernotec.driverschedule.service.scheduling.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VoucherCommonParam {
    CLOCK_ICON_IMG("images/icons/clock.png"),
    USERS_ICON_IMG("images/icons/users.png"),
    BOX_ICON_IMG("images/icons/box.png"),
    PHONE_ICON_IMG("images/icons/phone.png");

    private final String resourcePath;
}
