package com.kernotec.driverschedule.service.resource.rest;

import com.kernotec.driverschedule.common.rest.ApiSpec;

public class ResourceApiSpec {

    public static final class LocationSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/locations";
        public static final String TAG_NAME = "LOCATION";
        public static final String TAG_DESCRIPTION = "Location management";

        private LocationSpec() {
        }
    }

    public static final class ObservationSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/observations";
        public static final String TAG_NAME = "OBSERVATION";
        public static final String TAG_DESCRIPTION = "Observation management";

        private ObservationSpec() {
        }
    }

    public static final class ObservationTypeSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/observation-types";
        public static final String TAG_NAME = "OBSERVATION TYPE";
        public static final String TAG_DESCRIPTION = "Observation Type management";

        private ObservationTypeSpec() {
        }
    }

    public static final class PlaceCategorySpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/place-categories";
        public static final String TAG_NAME = "PLACE CATEGORY";
        public static final String TAG_DESCRIPTION = "Place Category management";

        private PlaceCategorySpec() {
        }
    }

    public static final class ReasonSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/reasons";
        public static final String TAG_NAME = "REASON";
        public static final String TAG_DESCRIPTION = "Reason management";

        private ReasonSpec() {
        }
    }

    public static final class ReasonTypeSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/reason-types";
        public static final String TAG_NAME = "REASON TYPE";
        public static final String TAG_DESCRIPTION = "Reason Type management";

        private ReasonTypeSpec() {
        }
    }

    public static final class VehicleSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/vehicles";
        public static final String TAG_NAME = "VEHICLE";
        public static final String TAG_DESCRIPTION = "Vehicle management";

        private VehicleSpec() {
        }
    }

    public static final class VehicleTypeSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/vehicle-types";
        public static final String TAG_NAME = "VEHICLE TYPE";
        public static final String TAG_DESCRIPTION = "Vehicle types management";

        private VehicleTypeSpec() {
        }
    }
}
