package com.kernotec.driverschedule.service.request.rest;

import com.kernotec.driverschedule.service.rest.ApiSpec;

public class ApiRequestSpec {

    public static final class RejectReasonSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/reject-reasons";
        public static final String TAG_NAME = "REJECT REASON";
        public static final String TAG_DESCRIPTION = "Reject reason management";

        private RejectReasonSpec() {
        }
    }

    public static final class RequestCoordSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/request-coords";
        public static final String TAG_NAME = "REQUEST COORD";
        public static final String TAG_DESCRIPTION = "Request Coord management";

        private RequestCoordSpec() {
        }
    }

    public static final class TransportationRequestSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/transportation-requests";
        public static final String TAG_NAME = "TRANSPORTATION REQUEST";
        public static final String TAG_DESCRIPTION = "Transportation request management";

        private TransportationRequestSpec() {
        }
    }

    public static final class TransportationRequestLogSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/transportation-request-logs";
        public static final String TAG_NAME = "TRANSPORTATION REQUEST LOG";
        public static final String TAG_DESCRIPTION = "Transportation Request Log management";

        private TransportationRequestLogSpec() {
        }
    }

    public static final class TransportationRequestStateSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/transportation-request-states";
        public static final String TAG_NAME = "TRANSPORTATION REQUEST STATE";
        public static final String TAG_DESCRIPTION = "Transportation request state management";

        private TransportationRequestStateSpec() {
        }
    }
}
