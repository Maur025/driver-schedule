package com.kernotec.driverscheduleservice.jpa.repository.notification;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.notification.NotificationConfiguration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationConfigurationRepository extends
    BaseRepository<NotificationConfiguration, UUID>
{

    Optional<NotificationConfiguration> findByTokenAndDeleted(String token, boolean deleted);

    List<NotificationConfiguration> findByPersonIdInAndActivedAndDeleted(Collection<UUID> personIds,
        boolean actived, boolean deleted);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        DELETE FROM NotificationConfiguration nc
        WHERE nc.id IN :ids
        """)
    void deleteAllByIdIn(@Param("ids") Collection<UUID> ids);
}
