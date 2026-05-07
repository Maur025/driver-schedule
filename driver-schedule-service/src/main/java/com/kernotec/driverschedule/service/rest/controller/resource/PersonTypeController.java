package com.kernotec.driverschedule.service.rest.controller.resource;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.service.jpa.entity.resource.PersonType;
import com.kernotec.driverschedule.service.jpa.service.resource.PersonTypeService;
import com.kernotec.driverschedule.service.rest.ApiSpec.PersonTypeSpec;
import com.kernotec.driverschedule.service.rest.dto.resource.response.person.type.PersonTypeResponse;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.person.type.PersonTypeResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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

@Tag(name = PersonTypeSpec.TAG_NAME, description = PersonTypeSpec.TAG_DESCRIPTION)
@RequestMapping(path = PersonTypeSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class PersonTypeController {

    private final PersonTypeService personTypeService;
    private final PersonTypeResponseMapper personTypeResponseMapper;

    @Operation(summary = "find all person types")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<PersonTypeResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<PersonType> personTypePage = personTypeService.findAll(pageable);

        return PageResponse.<PersonTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(personTypeResponseMapper.toResponse(personTypePage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(personTypePage.getTotalElements())
                .pages(personTypePage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find person types without pagination")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Deprecated
    public PageResponse<PersonTypeResponse> findAllWithoutPagination() {
        List<PersonType> personTypeList = personTypeService.findAll();

        return PageResponse.<PersonTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(personTypeResponseMapper.toResponse(personTypeList))
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{personTypeId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<PersonTypeResponse> findById(
        @PathVariable("personTypeId") UUID personTypeId)
    {
        PersonType personType = personTypeService.findByIdThrow(personTypeId);

        return SingleResponse.<PersonTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(personTypeResponseMapper.toResponse(personType))
            .build();
    }
}
