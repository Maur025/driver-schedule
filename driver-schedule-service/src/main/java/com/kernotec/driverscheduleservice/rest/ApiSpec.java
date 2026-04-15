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

    public static final class PlaceCategorySpec {

        public static final String BASE_PATH = ROOT_PATH + "/place-categories";
        public static final String TAG_NAME = "PLACE CATEGORY";
        public static final String TAG_DESCRIPTION = "Place Category management";

        private PlaceCategorySpec() {
        }
    }

    public static final class ReasonTypeSpec {

        public static final String BASE_PATH = ROOT_PATH + "/reason-types";
        public static final String TAG_NAME = "REASON TYPE";
        public static final String TAG_DESCRIPTION = "Reason Type management";

        private ReasonTypeSpec() {
        }
    }

    public static final class RequestCoordSpec {

        public static final String BASE_PATH = ROOT_PATH + "/request-coords";
        public static final String TAG_NAME = "REQUEST COORD";
        public static final String TAG_DESCRIPTION = "Request Coord management";

        private RequestCoordSpec() {
        }
    }

    public static final class TripAssignmentSpec {

        public static final String BASE_PATH = ROOT_PATH + "/trip-assignments";
        public static final String TAG_NAME = "TRIP ASSIGNMENT";
        public static final String TAG_DESCRIPTION = "Trip Assignment management";

        private TripAssignmentSpec() {
        }
    }

    public static final class TripSpec {

        public static final String BASE_PATH = ROOT_PATH + "/trips";
        public static final String TAG_NAME = "TRIP";
        public static final String TAG_DESCRIPTION = "Trip management";

        private TripSpec() {
        }
    }

    public static final class TripLogSpec {

        public static final String BASE_PATH = ROOT_PATH + "/trip-logs";
        public static final String TAG_NAME = "TRIP LOG";
        public static final String TAG_DESCRIPTION = "Trip Log management";

        private TripLogSpec() {
        }
    }

    public static final class TripStateSpec {

        public static final String BASE_PATH = ROOT_PATH + "/trip-states";
        public static final String TAG_NAME = "TRIP STATE";
        public static final String TAG_DESCRIPTION = "Trip State management";

        private TripStateSpec() {
        }
    }

    public static final class TransportationRequestLogSpec {

        public static final String BASE_PATH = ROOT_PATH + "/transportation-request-logs";
        public static final String TAG_NAME = "TRANSPORTATION REQUEST LOG";
        public static final String TAG_DESCRIPTION = "Transportation Request Log management";

        private TransportationRequestLogSpec() {
        }
    }

    public static final class ScheduleTransportationLogSpec {

        public static final String BASE_PATH = ROOT_PATH + "/schedule-transportation-logs";
        public static final String TAG_NAME = "SCHEDULE TRANSPORTATION LOG";
        public static final String TAG_DESCRIPTION = "Schedule Transportation Log management";

        private ScheduleTransportationLogSpec() {
        }
    }

    public static final class EmergencyReasonSpec {

        public static final String BASE_PATH = ROOT_PATH + "/emergency-reasons";
        public static final String TAG_NAME = "EMERGENCY REASON";
        public static final String TAG_DESCRIPTION = "Emergency reason management";

        private EmergencyReasonSpec() {
        }
    }

    public static final class EmergencyRejectReasonSpec {

        public static final String BASE_PATH = ROOT_PATH + "/emergency-reject-reasons";
        public static final String TAG_NAME = "EMERGENCY REJECT REASON";
        public static final String TAG_DESCRIPTION = "Emergency reject reason management";

        private EmergencyRejectReasonSpec() {
        }
    }

    public static final class TripEmergencySpec {

        public static final String BASE_PATH = ROOT_PATH + "/trip-emergencies";
        public static final String TAG_NAME = "TRIP EMERGENCY";
        public static final String TAG_DESCRIPTION = "Trip Emergency management";

        private TripEmergencySpec() {
        }
    }

    public static final class TripEmergencyLogSpec {

        public static final String BASE_PATH = ROOT_PATH + "/trip-emergency-logs";
        public static final String TAG_NAME = "TRIP EMERGENCY LOG";
        public static final String TAG_DESCRIPTION = "Trip Emergency Log management";

        private TripEmergencyLogSpec() {
        }
    }

    public static final class TripEmergencyStateSpec {

        public static final String BASE_PATH = ROOT_PATH + "/trip-emergency-states";
        public static final String TAG_NAME = "TRIP EMERGENCY STATES";
        public static final String TAG_DESCRIPTION = "Trip Emergency States management";

        private TripEmergencyStateSpec() {
        }
    }

    public static final class EmergencyResponseSpec {

        public static final String BASE_PATH = ROOT_PATH + "/emergency-responses";
        public static final String TAG_NAME = "EMERGENCY RESPONSE";
        public static final String TAG_DESCRIPTION = "Emergency Response management";

        private EmergencyResponseSpec() {
        }
    }

    public static final class EmergencyResponseTypeSpec {

        public static final String BASE_PATH = ROOT_PATH + "/emergency-response-types";
        public static final String TAG_NAME = "EMERGENCY RESPONSE TYPE";
        public static final String TAG_DESCRIPTION = "Emergency Response Type management";

        private EmergencyResponseTypeSpec() {
        }
    }

    public static final class ObservationSpec {

        public static final String BASE_PATH = ROOT_PATH + "/observations";
        public static final String TAG_NAME = "OBSERVATION";
        public static final String TAG_DESCRIPTION = "Observation management";

        private ObservationSpec() {
        }
    }

    public static final class ObservationTypeSpec {

        public static final String BASE_PATH = ROOT_PATH + "/observation-types";
        public static final String TAG_NAME = "OBSERVATION TYPE";
        public static final String TAG_DESCRIPTION = "Observation Type management";

        private ObservationTypeSpec() {
        }
    }

    public static final class TripObservationSpec {

        public static final String BASE_PATH = ROOT_PATH + "/trip-observations";
        public static final String TAG_NAME = "TRIP OBSERVATION";
        public static final String TAG_DESCRIPTION = "Trip Observation management";

        private TripObservationSpec() {
        }
    }
}
