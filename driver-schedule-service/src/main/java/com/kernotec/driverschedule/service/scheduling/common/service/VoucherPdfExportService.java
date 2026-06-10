package com.kernotec.driverschedule.service.scheduling.common.service;

import com.kernotec.driverschedule.report.service.ReportService;
import com.kernotec.driverschedule.service.scheduling.common.enums.VoucherCommonParam;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VoucherPdfExportService {

    private final ReportService reportService;

    public Map<String, Object> getVoucherParams(String zoneId) {
        Map<String, Object> params = reportService.getCommonParams(zoneId);

        params.put(
            VoucherCommonParam.CLOCK_ICON_IMG.toString(),
            reportService.imageToByteArray(VoucherCommonParam.CLOCK_ICON_IMG.getResourcePath())
        );
        params.put(
            VoucherCommonParam.USERS_ICON_IMG.toString(),
            reportService.imageToByteArray(VoucherCommonParam.USERS_ICON_IMG.getResourcePath())
        );
        params.put(
            VoucherCommonParam.BOX_ICON_IMG.toString(),
            reportService.imageToByteArray(VoucherCommonParam.BOX_ICON_IMG.getResourcePath())
        );
        params.put(
            VoucherCommonParam.PHONE_ICON_IMG.toString(),
            reportService.imageToByteArray(VoucherCommonParam.PHONE_ICON_IMG.getResourcePath())
        );

        return params;
    }
}
