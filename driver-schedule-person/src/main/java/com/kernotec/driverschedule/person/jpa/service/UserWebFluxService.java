package com.kernotec.driverschedule.person.jpa.service;

import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.user.api.client.rest.UserServiceApiClient;
import com.kernotec.driverschedule.user.api.spec.rest.dto.request.UserCreateRequest;
import com.kernotec.driverschedule.user.api.spec.rest.dto.request.UserDeleteRequest;
import com.kernotec.driverschedule.user.api.spec.rest.dto.request.UserUpdateRequest;
import com.kernotec.driverschedule.user.api.spec.rest.dto.response.UserCreateResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserWebFluxService {

    private final UserServiceApiClient userServiceApiClient;

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
}
