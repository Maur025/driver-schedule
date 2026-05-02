package com.kernotec.driverscheduleservice.notification.templates;

import com.kernotec.driverscheduleservice.jpa.enums.notification.CampaignRecipientEnum;
import java.util.Map;

public class RequestCreateNotificationTemplate {

    public static final String TITLE = "Nueva solicitud";
    public static final String BODY = "Se ha creado una nueva solicitud de transporte. Por favor, revisa los detalles y asigna un conductor.";
    public static final CampaignRecipientEnum RECIPIENT = CampaignRecipientEnum.ALL_SCHEDULERS;
}
