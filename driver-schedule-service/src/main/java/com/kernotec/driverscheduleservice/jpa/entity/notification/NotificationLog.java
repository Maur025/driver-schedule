package com.kernotec.driverscheduleservice.jpa.entity.notification;

import com.kernotec.driverscheduleservice.audit.user.BaseAuditEntityUser;
import com.kernotec.driverscheduleservice.jpa.enums.notification.NotificationLogStateEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notification_logs")
public class NotificationLog extends BaseAuditEntityUser {

    @Column(name = "notification_log_state", nullable = false)
    private NotificationLogStateEnum notificationLogState;

    @Column(name = "notification_configuration_id", nullable = false)
    private UUID notificationConfigurationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_configuration_id", referencedColumnName = "id",
                insertable = false, updatable = false)
    private NotificationConfiguration notificationConfiguration;

    @Column(name = "notification_campaign_id", nullable = false)
    private UUID notificationCampaignId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_campaign_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private NotificationCampaign notificationCampaign;
}
