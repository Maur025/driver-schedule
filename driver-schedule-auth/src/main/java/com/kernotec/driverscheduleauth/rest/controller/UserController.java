package com.kernotec.driverscheduleauth.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import com.kernotec.driverscheduleauth.rest.ApiSpec.UserSpec;
import com.kernotec.driverscheduleauth.rest.command.user.ProcessUserCreateRequestCmd;
import com.kernotec.driverscheduleauth.rest.dto.request.user.UserCreateRequest;
import com.kernotec.driverscheduleauth.rest.dto.response.user.UserResponse;
import com.kernotec.driverscheduleauth.rest.mapper.user.UserResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = UserSpec.TAG_NAME, description = UserSpec.TAG_DESCRIPTION)
@RequestMapping(path = UserSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserResponseMapper userResponseMapper;
    private final ProcessUserCreateRequestCmd processUserCreateRequestCmd;

    @Operation(summary = "find all users")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<UserResponse> findAllUsers(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        Page<User> userPage = userService.findAll(pageable);

        return PageResponse.<UserResponse>builder()
            .code(HttpStatus.OK.value())
            .data(userResponseMapper.toResponse(userPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(userPage.getTotalElements())
                .pages(userPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "get user by id")
    @GetMapping("{userId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<UserResponse> findById(@PathVariable UUID userId) {
        User user = userService.findByIdThrow(userId);

        return SingleResponse.<UserResponse>builder()
            .code(HttpStatus.OK.value())
            .data(userResponseMapper.toResponse(user))
            .build();
    }

    @Operation(summary = "save user")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResponse<UserResponse> save(@RequestBody UserCreateRequest request) {
        UUID userId = processUserCreateRequestCmd.withRequest(
                ProcessUserCreateRequestCmd.Request.builder()
                    .userCreateRequest(request)
                    .build())
            .execute();

        return SingleResponse.<UserResponse>builder()
            .code(HttpStatus.CREATED.value())
            .data(userResponseMapper.toResponse(userId))
            .build();
    }
}
