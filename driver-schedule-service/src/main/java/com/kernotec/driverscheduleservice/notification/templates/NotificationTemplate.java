package com.kernotec.driverscheduleservice.notification.templates;

import com.kernotec.driverscheduleservice.jpa.enums.notification.CampaignRecipientEnum;

public class NotificationTemplate {

    public static final class RequestCreateTemplate {

        public static final String TITLE = "Nueva solicitud";
        public static final String BODY = "Se ha creado una nueva solicitud de transporte. Está pendiente de aprobación, revisa los detalles.";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ALL_SCHEDULERS;

        private RequestCreateTemplate() {
        }
    }

    public static final class RequestRejectedTemplate {

        public static final String TITLE = "Solicitud rechazada";
        public static final String BODY = "Se ha rechazado tu solicitud de transporte. Por favor, revisa los detalles.";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ONLY_USER;

        private RequestRejectedTemplate() {
        }
    }

    public static final class RequestCancelledTemplate {

        public static final String TITLE = "Solicitud cancelada por el solicitante";
        public static final String BODY = "El solicitante cancelo su solicitud de transporte. Por favor, revisa los detalles.";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ALL_SCHEDULERS;

        private RequestCancelledTemplate() {
        }
    }

    public static final class ScheduleApprovedTemplate {

        public static final String TITLE = "Solicitud aprobada";
        public static final String BODY = "Se ha aprobado tu solicitud de transporte. Por favor, revisa los detalles.";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ONLY_USER;

        private ScheduleApprovedTemplate() {
        }
    }

    public static final class ScheduleRescheduleTemplate {

        public static final String TITLE = "Salida programada modificada";
        public static final String BODY = "Se hizo un ajuste en tu salida programada. Por favor, revisa los detalles.";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ONLY_USER;

        private ScheduleRescheduleTemplate() {
        }
    }

    public static final class ScheduleCancelledTemplate {

        public static final String TITLE = "Salida programada cancelada";
        public static final String BODY = "Se canceló tu salida programada. Por favor, revisa los detalles.";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ONLY_USER;

        private ScheduleCancelledTemplate() {
        }
    }

    public static final class TripEmergencyReportedTemplate {

        public static final String TITLE = "Emergencia reportada";
        public static final String BODY = "Se reportó una emergencia en uno de los viajes en curso. Por favor, revisa los detalles.";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ALL_SCHEDULERS;

        private TripEmergencyReportedTemplate() {
        }
    }

    public static final class TripEmergencyDissmisedTemplate {

        public static final String TITLE = "Emergencia desestimada";
        public static final String BODY = "Se desestimó tu reporte de emergencia. Por favor, revisa los detalles.";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ONLY_USER;

        private TripEmergencyDissmisedTemplate() {
        }
    }

    public static final class TripEmergencyHandledTemplate {

        public static final String TITLE = "Emergencia atendida";
        public static final String BODY = "Tu emergencia reportada fue revisada y atendida. Por favor, revisa los detalles.";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.ONLY_USER;

        private TripEmergencyHandledTemplate() {
        }
    }

    public static final class DriverAssignmentTemplate {

        public static final String TITLE = "Asignación de viaje";
        public static final String BODY = "Se te asignó un viaje. Por favor, revisa los detalles.";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.SOME_USERS;

        private DriverAssignmentTemplate() {
        }
    }

    public static final class DriverAssignmentCancelledTemplate {

        public static final String TITLE = "Asignación de viaje cancelada";
        public static final String BODY = "Se canceló tu asignacion al viaje programado. Por favor, revisa los detalles.";
        public static final CampaignRecipientEnum RECEIVER = CampaignRecipientEnum.SOME_USERS;

        private DriverAssignmentCancelledTemplate() {
        }
    }
}
