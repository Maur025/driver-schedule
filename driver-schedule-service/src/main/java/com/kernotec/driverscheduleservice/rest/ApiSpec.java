package com.kernotec.driverscheduleservice.rest;

public class ApiSpec {

    public static final String ROOT_PATH = "/api/driver-schedule";

    public static final class CancelReasonSpec {

        public static final String BASE_PATH = ROOT_PATH + "/cancel-reasons";
        public static final String TAG_NAME = "CANCEL REASON";
        public static final String TAG_DESCRIPTION = "Cancel Reason management";

        private CancelReasonSpec() {
        }
    }

    public static final class LocationSpec {

        public static final String BASE_PATH = ROOT_PATH + "/locations";
        public static final String TAG_NAME = "LOCATION";
        public static final String TAG_DESCRIPTION = "Location management";

        private LocationSpec() {
        }
    }

    public static final class PersonSpec {

        public static final String BASE_PATH = ROOT_PATH + "/persons";
        public static final String TAG_NAME = "PERSON";
        public static final String TAG_DESCRIPTION = "Person management";

        private PersonSpec() {
        }
    }

    public static final class PersonTypeSpec {

        public static final String BASE_PATH = ROOT_PATH + "/person-types";
        public static final String TAG_NAME = "PERSON TYPE";
        public static final String TAG_DESCRIPTION = "Person type management";

        private PersonTypeSpec() {
        }
    }

    public static final class ReasonSpec {

        public static final String BASE_PATH = ROOT_PATH + "/reasons";
        public static final String TAG_NAME = "REASON";
        public static final String TAG_DESCRIPTION = "Reason management";

        private ReasonSpec() {
        }
    }

    public static final class RejectReasonSpec {

        public static final String BASE_PATH = ROOT_PATH + "/reject-reasons";
        public static final String TAG_NAME = "REJECT REASON";
        public static final String TAG_DESCRIPTION = "Reject reason management";

        private RejectReasonSpec() {
        }
    }

    public static final class RescheduleReasonSpec {

        public static final String BASE_PATH = ROOT_PATH + "/reschedule-reasons";
        public static final String TAG_NAME = "RESCHEDULE REASON";
        public static final String TAG_DESCRIPTION = "Reschedule reason management";

        private RescheduleReasonSpec() {
        }
    }

    public static final class ScheduleTransportationSpec {

        public static final String BASE_PATH = ROOT_PATH + "/schedule-transportations";
        public static final String TAG_NAME = "SCHEDULE TRANSPORTATION";
        public static final String TAG_DESCRIPTION = "Schedule transportation management";

        private ScheduleTransportationSpec() {
        }
    }

    public static final class ScheduleTransportationStateSpec {

        public static final String BASE_PATH = ROOT_PATH + "/schedule-transportation-states";
        public static final String TAG_NAME = "SCHEDULE TRANSPORTATION STATE";
        public static final String TAG_DESCRIPTION = "Schedule transportation state management";

        private ScheduleTransportationStateSpec() {
        }
    }

    public static final class TransportationRequestSpec {

        public static final String BASE_PATH = ROOT_PATH + "/transportation-requests";
        public static final String TAG_NAME = "TRANSPORTATION REQUEST";
        public static final String TAG_DESCRIPTION = "Transportation request management";

        private TransportationRequestSpec() {
        }
    }

    public static final class TransportationRequestStateSpec {

        public static final String BASE_PATH = ROOT_PATH + "/transportation-request-states";
        public static final String TAG_NAME = "TRANSPORTATION REQUEST STATE";
        public static final String TAG_DESCRIPTION = "Transportation request state management";

        private TransportationRequestStateSpec() {
        }
    }

    public static final class VehicleSpec {

        public static final String BASE_PATH = ROOT_PATH + "/vehicles";
        public static final String TAG_NAME = "VEHICLE";
        public static final String TAG_DESCRIPTION = "Vehicle management";

        private VehicleSpec() {
        }
    }

    public static final class VehicleTypeSpec {

        public static final String BASE_PATH = ROOT_PATH + "/vehicle-types";
        public static final String TAG_NAME = "VEHICLE TYPE";
        public static final String TAG_DESCRIPTION = "Vehicle types management";

        private VehicleTypeSpec() {
        }
    }

    public static final class ReportSpec {

        public static final String BASE_PATH = ROOT_PATH + "/reports";
        public static final String TAG_NAME = "REPORT";
        public static final String TAG_DESCRIPTION = "Reports management";

        private ReportSpec() {
        }
    }

    public static final class ContactSpec {

        public static final String BASE_PATH = ROOT_PATH + "/contacts";
        public static final String TAG_NAME = "CONTACT";
        public static final String TAG_DESCRIPTION = "Contact management";

        private ContactSpec() {
        }
    }

    public static final class ContactCategorySpec {

        public static final String BASE_PATH = ROOT_PATH + "/contact-categories";
        public static final String TAG_NAME = "CONTACT CATEGORY";
        public static final String TAG_DESCRIPTION = "Contact Category management";

        private ContactCategorySpec() {
        }
    }

    public static final class LabelTypeSpec {

        public static final String BASE_PATH = ROOT_PATH + "/label-types";
        public static final String TAG_NAME = "LABEL TYPE";
        public static final String TAG_DESCRIPTION = "Label Type management";

        private LabelTypeSpec() {
        }
    }
}
