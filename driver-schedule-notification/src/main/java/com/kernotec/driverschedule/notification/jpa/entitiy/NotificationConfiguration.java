package com.kernotec.driverschedule.notification.jpa.entitiy;

import com.kernotec.driverschedule.common.audit.user.BaseAuditEntityUser;
import com.kernotec.driverschedule.notification.jpa.enums.PlatformEnum;
import com.kernotec.driverschedule.person.jpa.entity.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OptimisticLock;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notification_configurations")
public class NotificationConfiguration extends BaseAuditEntityUser {

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

    @Column(name = "actived", nullable = false)
    private boolean actived = false;

    @Column(name = "person_id", nullable = false)
    private UUID personId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Person person;
}
