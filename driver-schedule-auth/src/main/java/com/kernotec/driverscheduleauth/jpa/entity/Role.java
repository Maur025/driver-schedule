package com.kernotec.driverscheduleauth.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(name = "UC_realm_name_resource",
                                                             columnNames = {"realm_id", "name",
                                                                 "resource"}))
public class Role extends BaseAuditEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "resource", nullable = false)
    private String resource;

    @Column(name = "realm_id", nullable = false)
    private UUID realmId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "realm_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Realm realm;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_scopes",
               joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "scope_id", referencedColumnName = "id"))
    private Set<Scope> scopes;
}
