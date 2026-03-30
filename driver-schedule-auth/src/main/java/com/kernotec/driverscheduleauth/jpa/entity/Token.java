package com.kernotec.driverscheduleauth.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.driverscheduleauth.jpa.enums.TokenStateEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "tokens",
       uniqueConstraints = @UniqueConstraint(columnNames = {"token_id", "client_id", "user_id"}))
public class Token extends BaseAuditEntity {

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "token", nullable = false, columnDefinition = "TEXT")
    private String token;

    @Column(name = "token_id")
    private UUID tokenId;

    @Column(name = "token_parent_id")
    private UUID tokenParentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "token_parent_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Token tokenParent;

    @Column(name = "issued_at", nullable = false)
    private ZonedDateTime issuedAt;

    @Column(name = "expires_at", nullable = false)
    private ZonedDateTime expiresAt;

    @Column(name = "expires_in", nullable = false)
    private Long expiresIn;

    @OptimisticLock(excluded = true)
    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 20)
    private TokenStateEnum state;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private User user;
}
