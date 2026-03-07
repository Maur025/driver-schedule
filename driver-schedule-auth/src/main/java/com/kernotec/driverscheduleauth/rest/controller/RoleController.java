package com.kernotec.driverscheduleauth.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleauth.jpa.entity.Role;
import com.kernotec.driverscheduleauth.jpa.service.RealmService;
import com.kernotec.driverscheduleauth.jpa.service.RoleService;
import com.kernotec.driverscheduleauth.rest.ApiSpec.RoleSpec;
import com.kernotec.driverscheduleauth.rest.dto.response.role.RoleResponse;
import com.kernotec.driverscheduleauth.rest.mapper.role.RoleResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = RoleSpec.TAG_NAME, description = RoleSpec.TAG_DESCRIPTION)
@RequestMapping(path = RoleSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleResponseMapper roleResponseMapper;
    private final RealmService realmService;

    @Operation(summary = "find all roles")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<RoleResponse> findAllUsers(@PathVariable String realm,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") Boolean descending)
    {
        realmService.findRealmIdByNameInCache(realm);

        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        Page<Role> rolePage = roleService.findAll(pageable);

        return PageResponse.<RoleResponse>builder()
            .code(HttpStatus.OK.value())
            .data(roleResponseMapper.toResponse(rolePage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(rolePage.getTotalElements())
                .pages(rolePage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find role by id")
    @GetMapping("{roleId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<RoleResponse> findById(@PathVariable String realm,
        @PathVariable UUID roleId)
    {
        realmService.findRealmIdByNameInCache(realm);

        Role role = roleService.findByIdThrow(roleId);

        return SingleResponse.<RoleResponse>builder()
            .code(HttpStatus.OK.value())
            .data(roleResponseMapper.toResponse(role))
            .build();
    }
}
