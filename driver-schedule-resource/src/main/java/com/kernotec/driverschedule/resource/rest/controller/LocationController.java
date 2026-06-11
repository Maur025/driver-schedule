package com.kernotec.driverschedule.resource.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.MessageResponse;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.common.csv.imports.CsvImportCmd;
import com.kernotec.driverschedule.resource.authorize.annotation.CanCreateLocation;
import com.kernotec.driverschedule.resource.authorize.annotation.CanReadLocation;
import com.kernotec.driverschedule.resource.authorize.annotation.CanUpdateLocation;
import com.kernotec.driverschedule.resource.jpa.entity.Location;
import com.kernotec.driverschedule.resource.jpa.service.LocationService;
import com.kernotec.driverschedule.resource.rest.ResourceApiSpec.LocationSpec;
import com.kernotec.driverschedule.resource.rest.command.LocationCsvImportGetDtoCmd;
import com.kernotec.driverschedule.resource.rest.command.LocationCsvImportSaveCmd;
import com.kernotec.driverschedule.resource.rest.command.ProcessLocationCreateRequestCmd;
import com.kernotec.driverschedule.resource.rest.command.ProcessLocationUpdateRequestCmd;
import com.kernotec.driverschedule.resource.rest.dto.LocationCsvImportDto;
import com.kernotec.driverschedule.resource.rest.dto.request.LocationCreateRequest;
import com.kernotec.driverschedule.resource.rest.dto.request.LocationUpdateRequest;
import com.kernotec.driverschedule.resource.rest.dto.response.LocationResponse;
import com.kernotec.driverschedule.resource.rest.mapper.response.LocationResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = LocationSpec.TAG_NAME, description = LocationSpec.TAG_DESCRIPTION)
@RequestMapping(path = LocationSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final LocationResponseMapper locationResponseMapper;
    private final ProcessLocationCreateRequestCmd processLocationCreateRequestCmd;
    private final ProcessLocationUpdateRequestCmd processLocationUpdateRequestCmd;
    private final CsvImportCmd<LocationCsvImportDto> csvImportCmd;
    private final LocationCsvImportGetDtoCmd locationCsvImportGetDtoCmd;
    private final LocationCsvImportSaveCmd locationCsvImportSaveCmd;

    @Operation(summary = "find all locations")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CanReadLocation
    public PageResponse<LocationResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending,
        @RequestParam(name = "keyword", required = false) String keyword)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        Page<Location> locationPage = locationService.findAllByKeyword(keyword, pageable);

        return PageResponse.<LocationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(locationResponseMapper.toResponse(locationPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(locationPage.getTotalElements())
                .pages(locationPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find all without pagination")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @CanReadLocation
    @Deprecated
    public PageResponse<LocationResponse> findAllWithoutPagination()
    {
        Pageable pageable = PageableUtil.of(0, 20, "name", false);

        Page<Location> locationPage = locationService.findAll(pageable);

        return PageResponse.<LocationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(locationResponseMapper.toResponse(locationPage.getContent()))
            .build();
    }

    @Operation(summary = "find location by id")
    @GetMapping("{locationId}")
    @ResponseStatus(HttpStatus.OK)
    @CanReadLocation
    public SingleResponse<LocationResponse> findById(@PathVariable("locationId") UUID locationId) {
        Location location = locationService.findByIdThrow(locationId);

        return SingleResponse.<LocationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(locationResponseMapper.toResponse(location))
            .build();
    }

    @Operation(summary = "save location")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CanCreateLocation
    public SingleResponse<LocationResponse> save(@RequestBody LocationCreateRequest request) {
        UUID locationId = processLocationCreateRequestCmd.withRequest(
                ProcessLocationCreateRequestCmd.Request.builder()
                    .locationCreateRequest(request)
                    .build())
            .execute();

        return SingleResponse.<LocationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(locationResponseMapper.toResponse(locationId))
            .build();
    }

    @Operation(summary = "update location")
    @PutMapping("{locationId}")
    @ResponseStatus(HttpStatus.OK)
    @CanUpdateLocation
    public SingleResponse<LocationResponse> update(@PathVariable("locationId") UUID locationId,
        @RequestBody LocationUpdateRequest request)
    {
        processLocationUpdateRequestCmd.withRequest(
                ProcessLocationUpdateRequestCmd.Request.builder()
                    .locationId(locationId)
                    .locationUpdateRequest(request)
                    .build())
            .execute();

        return SingleResponse.<LocationResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Location updated successfully")
            .build();
    }

    @Operation(summary = "import locations of csv file")
    @PostMapping(value = "imports/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse importLocationsFromCsv(
        @RequestPart(value = "file") MultipartFile multipartFile)
    {
        csvImportCmd.withRequest(CsvImportCmd.Request.<LocationCsvImportDto>builder()
                .excelFile(multipartFile)
                .mapperCallback(csvData -> locationCsvImportGetDtoCmd.withRequest(
                        LocationCsvImportGetDtoCmd.Request.builder()
                            .csvData(csvData)
                            .build())
                    .execute())
                .saveCallback(dtoList -> locationCsvImportSaveCmd.withRequest(
                        LocationCsvImportSaveCmd.Request.builder()
                            .locationCsvImportDtoList(dtoList)
                            .build())
                    .execute())
                .build())
            .execute();

        return MessageResponse.builder()
            .code(HttpStatus.OK.value())
            .message("Locations imported successfully")
            .build();
    }
}
