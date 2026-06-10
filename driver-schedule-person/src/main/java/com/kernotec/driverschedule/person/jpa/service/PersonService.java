package com.kernotec.driverschedule.person.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.common.security.auth.SecurityAuthProvider;
import com.kernotec.driverschedule.common.util.CommonUtil;
import com.kernotec.driverschedule.person.exception.PersonException;
import com.kernotec.driverschedule.person.jpa.dto.PersonDto;
import com.kernotec.driverschedule.person.jpa.entity.Person;
import com.kernotec.driverschedule.person.jpa.enums.PersonTypeEnum;
import com.kernotec.driverschedule.person.jpa.mapper.PersonDtoFlatMapper;
import com.kernotec.driverschedule.person.jpa.repository.PersonRepository;
import com.kernotec.driverschedule.person.jpa.specification.PersonSpecification;
import com.kernotec.driverschedule.person.rest.dto.response.PersonLookupResponse;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PersonService extends BaseServiceImpl<Person, UUID> {

    private final PersonRepository repository;
    private final SecurityAuthProvider securityAuthProvider;
    private final PersonDtoFlatMapper personDtoFlatMapper;

    @Override
    protected String resourceName() {
        return "Person";
    }

    @Override
    protected BaseRepository<Person, UUID> repository() {
        return repository;
    }

    public Optional<Person> findByDocument(String document) {
        return repository.findByDocumentIgnoreCase(document);
    }

    public Optional<Person> findByDocument(String document, UUID excludePersonId) {
        if (excludePersonId == null) {
            return findByDocument(document);
        }

        return repository.findByDocumentIgnoreCaseAndIdNot(document, excludePersonId);
    }

    public List<Person> findAllByDocumentIn(List<String> documents) {
        return repository.findAllByDocumentInIgnoreCase(documents);
    }

    public Optional<Person> findByUserId(UUID userId) {
        return repository.findByUserId(userId);
    }

    public Person findByUserIdThrow(UUID userId) {
        return findByUserId(userId).orElseThrow(
            () -> new PersonException(
                "not.found.by.user.id", "'" + userId + "'",
                HttpStatus.BAD_REQUEST.value()
            ));
    }

    public UUID findIdByUserIdThrow(UUID userId) {
        return findByUserIdThrow(userId).getId();
    }

    public Page<Person> findAllByPersonType(PersonTypeEnum personType, Pageable pageable) {
        return repository.findAll(
            PersonSpecification.builder()
                .withPersonType(personType)
                .withDeleted(false), pageable
        );
    }

    public Page<PersonLookupResponse> findAllToLookup(String keyword, PersonTypeEnum personType,
        Pageable pageable)
    {
        String keywordStr = CommonUtil.getSafeString(keyword);
        String personTypeStr = personType != null ? personType.toString() : null;

        return repository.findAllToLookup(keywordStr, personTypeStr, pageable);
    }

    public UUID findIdByUserIdAuthenticateThrow() {
        UUID userId = securityAuthProvider.getUserId();

        return findIdByUserIdThrow(userId);
    }

    public List<Person> findAllByPersonTypesAndDeleted(Collection<PersonTypeEnum> personTypes,
        boolean deleted)
    {
        return repository.findAllByPersonTypesAndDeleted(
            personTypes.stream()
                .map(String::valueOf)
                .toList(), deleted
        );
    }

    public List<Person> findAllByPersonTypesToNotification(Collection<PersonTypeEnum> personTypes) {
        return findAllByPersonTypesAndDeleted(personTypes, false);
    }

    public List<PersonDto> findAllDtoByPersonTypesToNotification(Set<PersonTypeEnum> personTypes) {
        List<Person> personList = findAllByPersonTypesToNotification(personTypes);
        return personDtoFlatMapper.toDto(personList);
    }

    public List<Person> findByIdInAndDeletedAndPersonTypesNotIn(Collection<UUID> ids,
        boolean deleted, Collection<PersonTypeEnum> personTypes)
    {
        List<String> personTypesStr = personTypes.stream()
            .map(String::valueOf)
            .toList();

        return repository.findByIdInAndDeletedAndPersonTypesNotIn(ids, deleted, personTypesStr);
    }

    public List<Person> canNotBeUsedAsDriver(Collection<UUID> ids) {
        return findByIdInAndDeletedAndPersonTypesNotIn(ids, true, Set.of(PersonTypeEnum.DRIVER));
    }

    public Page<Person> findAllWithFilters(Boolean deleted, Pageable pageable) {
        return repository.findAll(
            PersonSpecification.builder()
                .withDeleted(deleted), pageable
        );
    }
}
