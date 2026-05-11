package com.kernotec.driverschedule.service.schedule.rest;

import com.kernotec.driverschedule.common.rest.ApiSpec;

public class ScheduleApiSpec {

    public static final class CancelReasonSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/cancel-reasons";
        public static final String TAG_NAME = "CANCEL REASON";
        public static final String TAG_DESCRIPTION = "Cancel Reason management";

        private CancelReasonSpec() {
        }
    }

    public static final class RescheduleReasonSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/reschedule-reasons";
        public static final String TAG_NAME = "RESCHEDULE REASON";
        public static final String TAG_DESCRIPTION = "Reschedule reason management";

        private RescheduleReasonSpec() {
        }
    }

    public static final class ScheduleTransportationSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/schedule-transportations";
        public static final String TAG_NAME = "SCHEDULE TRANSPORTATION";
        public static final String TAG_DESCRIPTION = "Schedule transportation management";

        private ScheduleTransportationSpec() {
        }
    }

    public static final class ScheduleTransportationLogSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/schedule-transportation-logs";
        public static final String TAG_NAME = "SCHEDULE TRANSPORTATION LOG";
        public static final String TAG_DESCRIPTION = "Schedule Transportation Log management";

        private ScheduleTransportationLogSpec() {
        }
    }

    public static final class ScheduleTransportationStateSpec {

        public static final String BASE_PATH =
            ApiSpec.ROOT_PATH + "/schedule-transportation-states";
        public static final String TAG_NAME = "SCHEDULE TRANSPORTATION STATE";
        public static final String TAG_DESCRIPTION = "Schedule transportation state management";

        private ScheduleTransportationStateSpec() {
        }
    }

    public static final class TripAssignmentSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/trip-assignments";
        public static final String TAG_NAME = "TRIP ASSIGNMENT";
        public static final String TAG_DESCRIPTION = "Trip Assignment management";

        private TripAssignmentSpec() {
        }
    }
}
