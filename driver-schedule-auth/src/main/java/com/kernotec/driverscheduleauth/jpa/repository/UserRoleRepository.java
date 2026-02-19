package com.kernotec.driverscheduleauth.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleauth.jpa.entity.UserRole;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends BaseRepository<UserRole, UUID> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        DELETE FROM UserRole ur
        WHERE ur.userId = :userId
        """)
    void deleteAllByUserId(@Param("userId") UUID userId);
}
