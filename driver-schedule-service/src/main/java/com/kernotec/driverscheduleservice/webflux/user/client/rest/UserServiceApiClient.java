package com.kernotec.driverscheduleservice.webflux.user.client.rest;

import com.kernotec.core.api.AbstractApiClient;
import com.kernotec.core.exception.DefaultException;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.config.AuthConfigProperties;
import com.kernotec.driverscheduleservice.webflux.config.ModuleAppProperties;
import com.kernotec.driverscheduleservice.webflux.config.ModuleAppProperties.ServiceUri;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.request.UserCreateRequest;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.request.UserDeleteRequest;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.request.UserUpdateRequest;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.response.UserCreateResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceApiClient extends AbstractApiClient {

    private final ModuleAppProperties moduleAppProperties;
    private final AuthConfigProperties authConfigProperties;
    private final WebClient webClient;

    public Mono<SingleResponse<UserCreateResponse>> saveUser(UserCreateRequest request) {
        ServiceUri authAppConfig = moduleAppProperties.getAppAuth();

        return webClient.post()
            .uri(uriBuilder -> uriBuilder.scheme(authAppConfig.scheme())
                .host(authAppConfig.host())
                .port(authAppConfig.port())
                .path("realms/{realm}/users")
                .build(authConfigProperties.getRealm()))
            .bodyValue(request)
            .retrieve()
            .onStatus(
                HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new DefaultException(
                        clientResponse.statusCode()
                            .value(), body
                    )))
            )
            .bodyToMono(new ParameterizedTypeReference<SingleResponse<UserCreateResponse>>() {
            })
            .doOnNext(response -> log.info("save user success"))
            .doOnError(error -> log.error("Error saving user: ", error));
    }

    public Mono<Void> deleteUser(UUID userId, UserDeleteRequest request)
    {
        ServiceUri authAppConfig = moduleAppProperties.getAppAuth();

        return webClient.method(HttpMethod.DELETE)
            .uri(uriBuilder -> uriBuilder.scheme(authAppConfig.scheme())
                .host(authAppConfig.host())
                .port(authAppConfig.port())
                .path("realms/{realm}/users/{userId}")
                .build(authConfigProperties.getRealm(), userId))
            .bodyValue(request)
            .retrieve()
            .onStatus(
                HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new DefaultException(
                        clientResponse.statusCode()
                            .value(), body
                    )))
            )
            .bodyToMono(Void.class)
            .doOnNext(response -> log.info("delete user success"))
            .doOnError(error -> log.error("Error deleting user: ", error));
    }

    public Mono<Void> updateUser(UUID userId, UserUpdateRequest request)
    {
        ServiceUri authAppConfig = moduleAppProperties.getAppAuth();

        return webClient.patch()
            .uri(uriBuilder -> uriBuilder.scheme(authAppConfig.scheme())
                .host(authAppConfig.host())
                .port(authAppConfig.port())
                .path("realms/{realm}/users/{userId}")
                .build(authConfigProperties.getRealm(), userId))
            .bodyValue(request)
            .retrieve()
            .onStatus(
                HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new DefaultException(
                        clientResponse.statusCode()
                            .value(), body
                    )))
            )
            .bodyToMono(Void.class)
            .doOnNext(response -> log.info("update user success"))
            .doOnError(error -> log.error("Error updating user: ", error));
    }
}
