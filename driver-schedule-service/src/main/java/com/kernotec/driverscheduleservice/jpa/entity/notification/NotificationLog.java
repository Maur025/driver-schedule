package com.kernotec.driverscheduleservice.jpa.entity.notification;

import com.kernotec.driverscheduleservice.audit.user.BaseAuditEntityUser;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Person;
import com.kernotec.driverscheduleservice.jpa.enums.notification.NotificationLogStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.notification.PlatformEnum;
import com.kernotec.driverscheduleservice.util.SafeZoneDateTimeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OptimisticLock;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notification_logs")
public class NotificationLog extends BaseAuditEntityUser {

    @OptimisticLock(excluded = true)
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_log_state", nullable = false)
    private NotificationLogStateEnum notificationLogState;

    @Column(name = "sent_at", nullable = false)
    @Convert(converter = SafeZoneDateTimeConverter.class)
    private ZonedDateTime sentAt;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "device_id", nullable = false, unique = true)
    private String deviceId;

    @OptimisticLock(excluded = true)
    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false)
    private PlatformEnum platform;

    @Column(name = "token", nullable = false, columnDefinition = "TEXT")
    private String token;

    @Column(name = "person_id", nullable = false)
    private UUID PersonId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Person person;

    @Column(name = "notification_campaign_id", nullable = false)
    private UUID notificationCampaignId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_campaign_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private NotificationCampaign notificationCampaign;
}
