package com.kernotec.driverscheduleservice.audit.user;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.driverscheduleservice.audit.user.json.AuditUserData;
import com.kernotec.driverscheduleservice.audit.user.listener.AuditUserDataListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OptimisticLock;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@MappedSuperclass
@EntityListeners({AuditUserDataListener.class})
public class BaseAuditEntityUser extends BaseAuditEntity {

    @OptimisticLock(excluded = true)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "created_by_user", columnDefinition = "jsonb")
    private AuditUserData createdByUser;

    @OptimisticLock(excluded = true)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "updated_by_user", columnDefinition = "jsonb")
    private AuditUserData updatedByUser;
}
