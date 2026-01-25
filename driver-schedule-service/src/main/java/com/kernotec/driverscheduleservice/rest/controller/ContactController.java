package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.command.contact.ContactUpdateCmd;
import com.kernotec.driverscheduleservice.jpa.entity.Contact;
import com.kernotec.driverscheduleservice.jpa.service.ContactService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.ContactSpec;
import com.kernotec.driverscheduleservice.rest.command.contact.ProcessContactDeleteRequestCmd;
import com.kernotec.driverscheduleservice.rest.dto.request.contact.ContactUpdateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.ContactResponse;
import com.kernotec.driverscheduleservice.rest.mapper.contact.ContactResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = ContactSpec.TAG_NAME, description = ContactSpec.TAG_DESCRIPTION)
@RequestMapping(path = ContactSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ContactController {

    private final ContactService contactService;
    private final ContactResponseMapper contactResponseMapper;
    private final ContactUpdateCmd contactUpdateCmd;
    private final ProcessContactDeleteRequestCmd processContactDeleteRequestCmd;

    @Operation(summary = "find all contacts")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ContactResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<Contact> contactPage = contactService.findAll(pageable);

        return PageResponse.<ContactResponse>builder()
            .code(HttpStatus.OK.value())
            .data(contactResponseMapper.toResponse(contactPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(contactPage.getTotalElements())
                .pages(contactPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find contact by id")
    @GetMapping("{contactId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ContactResponse> findById(@PathVariable UUID contactId) {
        Contact contact = contactService.findByIdThrow(contactId);

        return SingleResponse.<ContactResponse>builder()
            .code(HttpStatus.OK.value())
            .data(contactResponseMapper.toResponse(contact))
            .build();
    }

    @Operation(summary = "update contact")
    @PatchMapping("{contactId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ContactResponse> update(@PathVariable UUID contactId,
        @RequestBody ContactUpdateRequest request)
    {
        contactUpdateCmd.withRequest(ContactUpdateCmd.Request.builder()
                .contactId(contactId)
                .label(request.getLabel())
                .value(request.getValue())
                .build())
            .execute();

        return SingleResponse.<ContactResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Contact updated successfully")
            .build();
    }

    @Operation(summary = "delete contact")
    @DeleteMapping("{contactId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ContactResponse> delete(@PathVariable UUID contactId) {
        processContactDeleteRequestCmd.withRequest(ProcessContactDeleteRequestCmd.Request.builder()
                .contactId(contactId)
                .build())
            .execute();

        return SingleResponse.<ContactResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Contact deleted successfully")
            .build();
    }
}
