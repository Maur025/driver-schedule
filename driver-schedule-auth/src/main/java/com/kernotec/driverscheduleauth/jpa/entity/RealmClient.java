package com.kernotec.driverscheduleauth.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
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
@Table(name = "realm_clients")
public class RealmClient extends BaseAuditEntity {

    @Column(name = "realm_id", nullable = false)
    private UUID realmId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "realm_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Realm realm;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Client client;
}
