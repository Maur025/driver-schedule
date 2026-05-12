package com.kernotec.driverschedule.report.rest;

import com.kernotec.driverschedule.common.rest.ApiSpec;

public class ReportApiSpec {

    public static final class ReportSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/reports";
        public static final String TAG_NAME = "REPORT";
        public static final String TAG_DESCRIPTION = "Reports management";

        private ReportSpec() {
        }
    }
}
