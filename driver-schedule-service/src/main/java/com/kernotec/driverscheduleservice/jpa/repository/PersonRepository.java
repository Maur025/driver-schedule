package com.kernotec.driverscheduleservice.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.Person;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends BaseRepository<Person, UUID> {

    Optional<Person> findByDocumentIgnoreCase(String document);

    List<Person> findAllByDocumentInIgnoreCase(List<String> documents);

    Optional<Person> findByUserId(UUID userId);
}
