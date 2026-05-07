package com.kernotec.driverschedule.service.jpa.entity.notification;

import com.kernotec.driverschedule.service.audit.user.BaseAuditEntityUser;
import com.kernotec.driverschedule.service.jpa.entity.resource.Person;
import com.kernotec.driverschedule.service.jpa.enums.notification.PersonNotificationState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OptimisticLock;
import org.hibernate.type.SqlTypes;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "person_notifications")
public class PersonNotification extends BaseAuditEntityUser {

    @Column(name = "title", length = 512)
    private String title;

    @Column(name = "body", nullable = false, length = 2048)
    private String body;

    @Column(name = "sent_at")
    private ZonedDateTime sentAt;

    @Column(name = "read_at")
    private ZonedDateTime readAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data", columnDefinition = "jsonb")
    private Map<String, String> data;

    @OptimisticLock(excluded = true)
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private PersonNotificationState state;

    @Column(name = "message_id")
    private UUID messageId;

    @Column(name = "person_id", nullable = false)
    private UUID personId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Person person;
}
