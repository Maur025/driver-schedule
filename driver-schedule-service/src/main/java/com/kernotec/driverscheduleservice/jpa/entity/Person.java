package com.kernotec.driverscheduleservice.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "person")
public class Person extends BaseAuditEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "document", nullable = false)
    private String document;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToMany
    @JoinTable(name = "person_assign_types",
               joinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "person_type_id",
                                                referencedColumnName = "id"))
    private Set<PersonType> personTypes;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Contact> contacts;
}