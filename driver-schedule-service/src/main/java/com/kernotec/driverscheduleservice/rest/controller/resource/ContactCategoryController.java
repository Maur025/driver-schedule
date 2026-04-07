package com.kernotec.driverscheduleservice.rest.controller.resource;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.jpa.entity.resource.ContactCategory;
import com.kernotec.driverscheduleservice.jpa.service.resource.ContactCategoryService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.ContactCategorySpec;
import com.kernotec.driverscheduleservice.rest.dto.common.response.LookupResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.contact.category.ContactCategoryLookupResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.contact.category.ContactCategoryResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.contact.category.ContactCategoryResponseMapper;
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

@Tag(name = ContactCategorySpec.TAG_NAME, description = ContactCategorySpec.TAG_DESCRIPTION)
@RequestMapping(path = ContactCategorySpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ContactCategoryController {


    private final ContactCategoryService contactCategoryService;
    private final ContactCategoryResponseMapper contactCategoryResponseMapper;

    @Operation(summary = "find all contact categories")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ContactCategoryResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<ContactCategory> contactCategoryPage = contactCategoryService.findAll(pageable);

        return PageResponse.<ContactCategoryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(contactCategoryResponseMapper.toResponse(contactCategoryPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(contactCategoryPage.getTotalElements())
                .pages(contactCategoryPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find contact category by id")
    @GetMapping("{contactCategoryId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ContactCategoryResponse> findById(
        @PathVariable("contactCategoryId") UUID contactCategoryId)
    {
        ContactCategory contactCategory = contactCategoryService.findByIdThrow(contactCategoryId);

        return SingleResponse.<ContactCategoryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(contactCategoryResponseMapper.toResponse(contactCategory))
            .build();
    }

    @Operation(summary = "find all to lookup")
    @GetMapping("lookup")
    @ResponseStatus(HttpStatus.OK)
    public LookupResponse<List<ContactCategoryLookupResponse>> findAllToLookup(
        @RequestParam(name = "keyword", required = false) String keyword)
    {
        Pageable pageable = PageableUtil.of(0, 500, "name", false);
        Page<ContactCategoryLookupResponse> contactCategoryPage = contactCategoryService.findAllToLookup(
            keyword, pageable);

        return LookupResponse.<List<ContactCategoryLookupResponse>>builder()
            .code(HttpStatus.OK.value())
            .data(contactCategoryPage.getContent())
            .build();
    }
}
