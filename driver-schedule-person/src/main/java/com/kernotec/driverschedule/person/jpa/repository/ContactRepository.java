package com.kernotec.driverschedule.person.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.person.jpa.entity.Contact;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends BaseRepository<Contact, UUID> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        DELETE FROM Contact c
        WHERE c.personId = :personId
        """)
    void deleteAllByPersonId(@Param("personId") UUID personId);
}
