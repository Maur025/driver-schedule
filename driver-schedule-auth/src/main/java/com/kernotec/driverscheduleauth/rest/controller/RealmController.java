package com.kernotec.driverscheduleauth.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleauth.jpa.entity.Realm;
import com.kernotec.driverscheduleauth.jpa.service.RealmService;
import com.kernotec.driverscheduleauth.rest.ApiSpec.RealmSpec;
import com.kernotec.driverscheduleauth.rest.dto.response.realm.RealmResponse;
import com.kernotec.driverscheduleauth.rest.mapper.realm.RealmResponseMapper;
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

@Tag(name = RealmSpec.TAG_NAME, description = RealmSpec.TAG_DESCRIPTION)
@RequestMapping(path = RealmSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class RealmController {

    private final RealmService realmService;
    private final RealmResponseMapper realmResponseMapper;

    @Operation(summary = "find all realms")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<RealmResponse> findAllUsers(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        Page<Realm> realmPage = realmService.findAll(pageable);

        return PageResponse.<RealmResponse>builder()
            .code(HttpStatus.OK.value())
            .data(realmResponseMapper.toResponse(realmPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(realmPage.getTotalElements())
                .pages(realmPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find realm by id")
    @GetMapping("{realmId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<RealmResponse> findById(@PathVariable UUID realmId) {
        Realm realm = realmService.findByIdThrow(realmId);

        return SingleResponse.<RealmResponse>builder()
            .code(HttpStatus.OK.value())
            .data(realmResponseMapper.toResponse(realm))
            .build();
    }
}
