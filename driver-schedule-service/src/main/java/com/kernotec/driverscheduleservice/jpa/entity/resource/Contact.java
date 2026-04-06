package com.kernotec.driverscheduleservice.jpa.entity.resource;

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
@Setter
@Getter
@Entity
@Table(name = "contacts")
public class Contact extends BaseAuditEntity {

    @Column(name = "value", nullable = false, length = 512)
    private String value;

    @Column(name = "label_type_id", nullable = false)
    private UUID labelTypeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "label_type_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private LabelType labelType;

    @Column(name = "contact_category_id", nullable = false)
    private UUID contactCategoryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contact_category_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private ContactCategory contactCategory;

    @Column(name = "person_id", nullable = false)
    private UUID personId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Person person;
}
