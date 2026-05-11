package com.kernotec.driverschedule.service.trip.rest;

import com.kernotec.driverschedule.common.rest.ApiSpec;

public class TripApiSpec {

    public static final class EmergencyReasonSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/emergency-reasons";
        public static final String TAG_NAME = "EMERGENCY REASON";
        public static final String TAG_DESCRIPTION = "Emergency reason management";

        private EmergencyReasonSpec() {
        }
    }

    public static final class EmergencyRejectReasonSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/emergency-reject-reasons";
        public static final String TAG_NAME = "EMERGENCY REJECT REASON";
        public static final String TAG_DESCRIPTION = "Emergency reject reason management";

        private EmergencyRejectReasonSpec() {
        }
    }

    public static final class EmergencyResponseSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/emergency-responses";
        public static final String TAG_NAME = "EMERGENCY RESPONSE";
        public static final String TAG_DESCRIPTION = "Emergency Response management";

        private EmergencyResponseSpec() {
        }
    }

    public static final class EmergencyResponseTypeSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/emergency-response-types";
        public static final String TAG_NAME = "EMERGENCY RESPONSE TYPE";
        public static final String TAG_DESCRIPTION = "Emergency Response Type management";

        private EmergencyResponseTypeSpec() {
        }
    }

    public static final class TripSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/trips";
        public static final String TAG_NAME = "TRIP";
        public static final String TAG_DESCRIPTION = "Trip management";

        private TripSpec() {
        }
    }

    public static final class TripEmergencySpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/trip-emergencies";
        public static final String TAG_NAME = "TRIP EMERGENCY";
        public static final String TAG_DESCRIPTION = "Trip Emergency management";

        private TripEmergencySpec() {
        }
    }

    public static final class TripEmergencyLogSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/trip-emergency-logs";
        public static final String TAG_NAME = "TRIP EMERGENCY LOG";
        public static final String TAG_DESCRIPTION = "Trip Emergency Log management";

        private TripEmergencyLogSpec() {
        }
    }

    public static final class TripEmergencyStateSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/trip-emergency-states";
        public static final String TAG_NAME = "TRIP EMERGENCY STATES";
        public static final String TAG_DESCRIPTION = "Trip Emergency States management";

        private TripEmergencyStateSpec() {
        }
    }

    public static final class TripLogSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/trip-logs";
        public static final String TAG_NAME = "TRIP LOG";
        public static final String TAG_DESCRIPTION = "Trip Log management";

        private TripLogSpec() {
        }
    }

    public static final class TripObservationSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/trip-observations";
        public static final String TAG_NAME = "TRIP OBSERVATION";
        public static final String TAG_DESCRIPTION = "Trip Observation management";

        private TripObservationSpec() {
        }
    }

    public static final class TripStateSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/trip-states";
        public static final String TAG_NAME = "TRIP STATE";
        public static final String TAG_DESCRIPTION = "Trip State management";

        private TripStateSpec() {
        }
    }
}
