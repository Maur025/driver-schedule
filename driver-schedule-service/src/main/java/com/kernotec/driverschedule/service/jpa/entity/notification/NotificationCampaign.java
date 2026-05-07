package com.kernotec.driverschedule.service.jpa.entity.notification;

import com.kernotec.driverschedule.service.audit.user.BaseAuditEntityUser;
import com.kernotec.driverschedule.service.jpa.enums.notification.CampaignRecipientEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OptimisticLock;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notification_campaigns")
public class NotificationCampaign extends BaseAuditEntityUser {

    @Column(name = "title", nullable = false, length = 512)
    private String title;

    @Column(name = "body", nullable = false, length = 2048)
    private String body;

    @OptimisticLock(excluded = true)
    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_recipient", nullable = false)
    private CampaignRecipientEnum campaignRecipient;

    @Column(name = "person_ids", columnDefinition = "TEXT")
    private String personIds;

    @OneToMany(mappedBy = "notificationCampaign", fetch = FetchType.LAZY)
    private List<NotificationLog> notificationLogs;
}
