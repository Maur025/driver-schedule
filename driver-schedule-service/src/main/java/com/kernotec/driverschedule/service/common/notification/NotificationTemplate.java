package com.kernotec.driverschedule.service.common.notification;

import com.kernotec.driverschedule.notification.jpa.enums.CampaignRecipientEnum;
import java.util.Map;

public class NotificationTemplate {

    public static final class RequestCreateTemplate {

        public static final String TITLE = "notification.title.request.created";
        public static final String BODY = "notification.message.request.created";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ALL_SCHEDULERS;
        public static final Map<String, String> MAP_DATA = Map.of(
            "referenceType", "request", "screen", "request/%s");

        private RequestCreateTemplate() {
        }
    }

    public static final class RequestRejectedTemplate {

        public static final String TITLE = "notification.title.request.rejected";
        public static final String BODY = "notification.message.request.rejected";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ONLY_USER;
        public static final Map<String, String> MAP_DATA = Map.of(
            "referenceType", "request", "screen", "request/%s");

        private RequestRejectedTemplate() {
        }
    }

    public static final class RequestCancelledTemplate {

        public static final String TITLE = "notification.title.request.cancelled";
        public static final String BODY = "notification.message.request.cancelled";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ALL_SCHEDULERS;
        public static final Map<String, String> MAP_DATA = Map.of(
            "referenceType", "request", "screen", "request/%s");

        private RequestCancelledTemplate() {
        }
    }

    public static final class ScheduleApprovedTemplate {

        public static final String TITLE = "notification.title.schedule.scheduled";
        public static final String BODY = "notification.message.schedule.scheduled";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ONLY_USER;
        public static final Map<String, String> MAP_DATA = Map.of(
            "referenceType", "schedule", "screen", "schedule/%s");

        private ScheduleApprovedTemplate() {
        }
    }

    public static final class ScheduleRescheduleTemplate {

        public static final String TITLE = "notification.title.schedule.rescheduled";
        public static final String BODY = "notification.message.schedule.rescheduled";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ONLY_USER;
        public static final Map<String, String> MAP_DATA = Map.of(
            "referenceType", "schedule", "screen", "schedule/%s");

        private ScheduleRescheduleTemplate() {
        }
    }

    public static final class ScheduleCancelledTemplate {

        public static final String TITLE = "notification.title.schedule.cancelled";
        public static final String BODY = "notification.message.schedule.cancelled";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ONLY_USER;
        public static final Map<String, String> MAP_DATA = Map.of(
            "referenceType", "schedule", "screen", "schedule/%s");

        private ScheduleCancelledTemplate() {
        }
    }

    public static final class TripEmergencyReportedTemplate {

        public static final String TITLE = "notification.title.trip.emergency.reported";
        public static final String BODY = "notification.message.trip.emergency.reported";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ALL_SCHEDULERS;
        public static final Map<String, String> MAP_DATA = Map.of(
            "referenceType", "trip-emergency", "screen", "trip-emergency/%s");

        private TripEmergencyReportedTemplate() {
        }
    }

    public static final class TripEmergencyDissmisedTemplate {

        public static final String TITLE = "notification.title.trip.emergency.dismissed";
        public static final String BODY = "notification.message.trip.emergency.dismissed";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ONLY_USER;
        public static final Map<String, String> MAP_DATA = Map.of(
            "referenceType", "trip-emergency", "screen", "trip-emergency/%s");

        private TripEmergencyDissmisedTemplate() {
        }
    }

    public static final class TripEmergencyHandledTemplate {

        public static final String TITLE = "notification.title.trip.emergency.handled";
        public static final String BODY = "notification.message.trip.emergency.handled";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ONLY_USER;
        public static final Map<String, String> MAP_DATA = Map.of(
            "referenceType", "trip-emergency", "screen", "trip-emergency/%s");

        private TripEmergencyHandledTemplate() {
        }
    }

    public static final class DriverAssignmentTemplate {

        public static final String TITLE = "notification.title.driver.assignment.assigned";
        public static final String BODY = "notification.message.driver.assignment.assigned";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.SOME_USERS;
        public static final Map<String, String> MAP_DATA = Map.of(
            "referenceType", "schedule", "screen", "schedule/%s");

        private DriverAssignmentTemplate() {
        }
    }

    public static final class DriverAssignmentCancelledTemplate {

        public static final String TITLE = "notification.title.driver.assignment.cancelled";
        public static final String BODY = "notification.message.driver.assignment.cancelled";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.SOME_USERS;
        public static final Map<String, String> MAP_DATA = Map.of(
            "referenceType", "schedule", "screen", "schedule/%s");

        private DriverAssignmentCancelledTemplate() {
        }
    }
}
