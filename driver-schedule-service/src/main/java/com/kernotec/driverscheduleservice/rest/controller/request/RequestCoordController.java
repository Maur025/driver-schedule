package com.kernotec.driverscheduleservice.rest.controller.request;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.jpa.entity.request.RequestCoord;
import com.kernotec.driverscheduleservice.jpa.service.request.RequestCoordService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.RequestCoordSpec;
import com.kernotec.driverscheduleservice.rest.dto.request.response.request.coord.RequestCoordResponse;
import com.kernotec.driverscheduleservice.rest.mapper.request.response.request.coord.RequestCoordResponseMapper;
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

@Tag(name = RequestCoordSpec.TAG_NAME, description = RequestCoordSpec.TAG_DESCRIPTION)
@RequestMapping(path = RequestCoordSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class RequestCoordController {

    private final RequestCoordService requestCoordService;
    private final RequestCoordResponseMapper requestCoordResponseMapper;

    @Operation(summary = "find all request12 coords")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<RequestCoordResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<RequestCoord> reasonTypePage = requestCoordService.findAll(pageable);

        return PageResponse.<RequestCoordResponse>builder()
            .code(HttpStatus.OK.value())
            .data(requestCoordResponseMapper.toResponse(reasonTypePage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(reasonTypePage.getTotalElements())
                .pages(reasonTypePage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find request12 coord by id")
    @GetMapping("{requestCoordId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<RequestCoordResponse> findById(
        @PathVariable("requestCoordId") UUID requestCoordId)
    {
        RequestCoord requestCoord = requestCoordService.findByIdThrow(requestCoordId);

        return SingleResponse.<RequestCoordResponse>builder()
            .code(HttpStatus.OK.value())
            .data(requestCoordResponseMapper.toResponse(requestCoord))
            .build();
    }
}
