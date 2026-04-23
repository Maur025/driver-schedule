package com.kernotec.driverscheduleservice.jpa.service.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.common.security.SecurityAuthProvider;
import com.kernotec.driverscheduleservice.exception.resource.PersonException;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Person;
import com.kernotec.driverscheduleservice.jpa.enums.resource.PersonTypeEnum;
import com.kernotec.driverscheduleservice.jpa.repository.resource.PersonRepository;
import com.kernotec.driverscheduleservice.jpa.specification.resource.PersonSpecification;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.person.PersonLookupResponse;
import com.kernotec.driverscheduleservice.util.CommonUtil;
import com.kernotec.driverscheduleservice.webflux.user.client.rest.UserServiceApiClient;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.request.UserCreateRequest;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.request.UserDeleteRequest;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.request.UserUpdateRequest;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.response.UserCreateResponse;
import java.util.List;
import java.util.Optional;
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
    private final UserServiceApiClient userServiceApiClient;
    private final SecurityAuthProvider securityAuthProvider;

    @Override
    protected String resourceName() {
        return "Person";
    }

    @Override
    protected BaseRepository<Person, UUID> repository() {
        return repository;
    }

    public UserCreateResponse saveUserFromPerson(UserCreateRequest request) {
        return userServiceApiClient.saveUser(request)
            .map(SingleResponse::getData)
            .block();
    }

    public void deleteUserFromPerson(UUID userId, UserDeleteRequest request) {
        userServiceApiClient.deleteUser(userId, request)
            .block();
    }

    public void updateUserFromPerson(UUID userId, UserUpdateRequest request) {
        userServiceApiClient.updateUser(userId, request)
            .block();
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
                .withPersonType(personType), pageable
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
}
