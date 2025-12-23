package com.kernotec.driverscheduleservice.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.Person;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends BaseRepository<Person, UUID> {

    Optional<Person> findByDocumentIgnoreCase(String document);

    Optional<Person> findByUserId(UUID userId);
}
