package com.kernotec.driverschedule.service.jpa.repository.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.jpa.entity.resource.Person;
import com.kernotec.driverschedule.service.rest.dto.resource.response.person.PersonLookupResponse;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends BaseRepository<Person, UUID> {

    Optional<Person> findByDocumentIgnoreCase(String document);

    List<Person> findAllByDocumentInIgnoreCase(List<String> documents);

    Optional<Person> findByDocumentIgnoreCaseAndIdNot(String document, UUID id);

    Optional<Person> findByUserId(UUID userId);

    @Query("""
        SELECT p.id as id, p.name as name, p.lastName as lastName
        FROM Person p
        INNER JOIN PersonAssignType pat ON p.id = pat.personId
        INNER JOIN PersonType pt ON pt.id = pat.personTypeId
        WHERE p.deleted = false
        AND (:personType IS NULL OR pt.code = :personType)
        AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%',:keyword,'%'))
               OR LOWER(p.lastName) LIKE LOWER(CONCAT('%',:keyword,'%')))
        GROUP BY p.id, p.name, p.lastName
        """)
    Page<PersonLookupResponse> findAllToLookup(@Param("keyword") String keyword,
        @Param("personType") String personType, Pageable pageable);

    @Query("""
        SELECT p
        FROM Person p
        INNER JOIN PersonAssignType pat ON pat.personId = p.id
        INNER JOIN PersonType pt ON pt.id = pat.personTypeId
        WHERE p.deleted = :deleted
        AND pt.code IN :personTypes
        GROUP BY p.id, p.name, p.lastName
        """)
    List<Person> findAllByPersonTypesAndDeleted(
        @Param("personTypes") Collection<String> personTypes, @Param("deleted") boolean deleted);

    @Query("""
        SELECT p
        FROM Person p
        INNER JOIN PersonAssignType pat ON pat.personId = p.id
        INNER JOIN PersonType pt ON pt.id = pat.personTypeId
        WHERE p.id IN :ids
        AND p.deleted = :deleted
        AND pt.code NOT IN :personTypes
        GROUP BY p.id, p.name, p.lastName
        """)
    List<Person> findByIdInAndDeletedAndPersonTypesNotIn(@Param("ids") Collection<UUID> ids,
        @Param("deleted") boolean deleted, @Param("personTypes") Collection<String> personTypes);
}
