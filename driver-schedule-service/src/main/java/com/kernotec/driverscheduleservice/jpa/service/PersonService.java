package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.jpa.entity.Person;
import com.kernotec.driverscheduleservice.jpa.repository.PersonRepository;
import com.kernotec.driverscheduleservice.webflux.user.client.rest.UserServiceApiClient;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.request.UserCreateRequest;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.response.UserCreateResponse;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PersonService extends BaseServiceImpl<Person, UUID> {

    private final PersonRepository repository;
    private final UserServiceApiClient userServiceApiClient;

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
}
