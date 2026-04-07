package com.kernotec.driverscheduleservice.rest.controller.resource;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.MessageResponse;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.common.annotation.person.CanCreatePerson;
import com.kernotec.driverscheduleservice.common.annotation.person.CanReadPerson;
import com.kernotec.driverscheduleservice.common.annotation.person.CanUpdatePerson;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Person;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.enums.resource.PersonTypeEnum;
import com.kernotec.driverscheduleservice.jpa.service.resource.PersonService;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.PersonSpec;
import com.kernotec.driverscheduleservice.rest.command.csv.imports.CsvImportCmd;
import com.kernotec.driverscheduleservice.rest.command.resource.person.PersonCsvImportGetDtoCmd;
import com.kernotec.driverscheduleservice.rest.command.resource.person.PersonCsvImportSaveCmd;
import com.kernotec.driverscheduleservice.rest.command.resource.person.ProcessPersonCreateRequestCmd;
import com.kernotec.driverscheduleservice.rest.command.resource.person.ProcessPersonUpdateRequestCmd;
import com.kernotec.driverscheduleservice.rest.dto.resource.PersonCsvImportDto;
import com.kernotec.driverscheduleservice.rest.dto.resource.request.person.PersonCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.resource.request.person.PersonScheduleConflictRequest;
import com.kernotec.driverscheduleservice.rest.dto.resource.request.person.PersonUpdateRequest;
import com.kernotec.driverscheduleservice.rest.dto.common.response.LookupResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.person.PersonLookupResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.person.PersonResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.person.PersonScheduleConflictResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.person.PersonResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationResponseMapper;
import com.kernotec.driverscheduleservice.util.ZonedDateTimeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Tag(name = PersonSpec.TAG_NAME, description = PersonSpec.TAG_DESCRIPTION)
@RequestMapping(path = PersonSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final ScheduleTransportationService scheduleTransportationService;
    private final ZonedDateTimeUtil zonedDateTimeUtil;

    private final PersonResponseMapper personResponseMapper;
    private final ScheduleTransportationResponseMapper scheduleTransportationResponseMapper;

    private final ProcessPersonCreateRequestCmd processPersonCreateRequestCmd;
    private final CsvImportCmd<PersonCsvImportDto> csvImportCmd;
    private final PersonCsvImportGetDtoCmd personCsvImportGetDtoCmd;
    private final PersonCsvImportSaveCmd personCsvImportSaveCmd;
    private final ProcessPersonUpdateRequestCmd processPersonUpdateRequestCmd;

    @Operation(summary = "find all persons")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CanReadPerson
    public PageResponse<PersonResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<Person> personPage = personService.findAll(pageable);

        return PageResponse.<PersonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(personResponseMapper.toResponse(personPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(personPage.getTotalElements())
                .pages(personPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find persons without pagination")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @CanReadPerson
    @Deprecated
    public PageResponse<PersonResponse> findAllWithoutPagination(
        @RequestParam(name = "personType", required = false) PersonTypeEnum personType)
    {
        Pageable pageable = PageableUtil.of(0, 20, "name", false);

        Page<Person> personPage = personService.findAllByPersonType(personType, pageable);

        return PageResponse.<PersonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(personResponseMapper.toResponse(personPage.getContent()))
            .build();
    }

    @Operation(summary = "find persons to lookup")
    @GetMapping("lookup")
    @ResponseStatus(HttpStatus.OK)
    @CanReadPerson
    public LookupResponse<List<PersonLookupResponse>> findAllToLookup(
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "personType", required = false) PersonTypeEnum personType)
    {
        Pageable pageable = PageableUtil.of(0, 500, "lastName", false);

        Page<PersonLookupResponse> personLookupResponsePage = personService.findAllToLookup(
            keyword, personType, pageable);

        return LookupResponse.<List<PersonLookupResponse>>builder()
            .code(HttpStatus.OK.value())
            .data(personLookupResponsePage.getContent())
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{personId}")
    @ResponseStatus(HttpStatus.OK)
    @CanReadPerson
    public SingleResponse<PersonResponse> findById(@PathVariable("personId") UUID personId)
    {
        Person person = personService.findByIdThrow(personId);

        return SingleResponse.<PersonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(personResponseMapper.toResponse(person))
            .build();
    }

    @Operation(summary = "save person")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CanCreatePerson
    public SingleResponse<PersonResponse> save(@RequestBody PersonCreateRequest request) {
        UUID personId = processPersonCreateRequestCmd.withRequest(
                ProcessPersonCreateRequestCmd.Request.builder()
                    .personCreateRequest(request)
                    .build())
            .execute();

        return SingleResponse.<PersonResponse>builder()
            .code(HttpStatus.CREATED.value())
            .data(personResponseMapper.toResponse(personId))
            .build();
    }

    @Operation(summary = "find driver schedule conflicts")
    @PostMapping("{driverId}/schedule-conflicts")
    @ResponseStatus(HttpStatus.OK)
    @CanReadPerson
    public SingleResponse<PersonScheduleConflictResponse> findPersonScheduleConflicts(
        @PathVariable("driverId") UUID driverId, @RequestBody PersonScheduleConflictRequest request)
    {
        ZonedDateTime from = zonedDateTimeUtil.getNewOfDateAndTime(
            request.getRequestedDate(), request.getConflictValidationFrom(), request.getZoneId());

        ZonedDateTime to = zonedDateTimeUtil.getNewOfDateAndTime(
            request.getRequestedDate(), request.getConflictValidationTo(), request.getZoneId());

        List<ScheduleTransportation> scheduleTransportationList = scheduleTransportationService.findConflictByDriverId(
            driverId, from, to, request.getZoneId(), request.getScheduleTransportationExcludeId());

        return SingleResponse.<PersonScheduleConflictResponse>builder()
            .code(HttpStatus.OK.value())
            .data(PersonScheduleConflictResponse.builder()
                .hasConflict(!scheduleTransportationList.isEmpty())
                .scheduleTransportationConflicts(
                    scheduleTransportationResponseMapper.toResponse(scheduleTransportationList))
                .build())
            .build();
    }

    @Operation(summary = "import persons of csv file")
    @PostMapping(value = "imports/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @CanCreatePerson
    public MessageResponse importPersonsFromCsvFile(@RequestPart(value = "file") MultipartFile file)
    {
        csvImportCmd.withRequest(CsvImportCmd.Request.<PersonCsvImportDto>builder()
                .excelFile(file)
                .mapperCallback(csvData -> personCsvImportGetDtoCmd.withRequest(
                        PersonCsvImportGetDtoCmd.Request.builder()
                            .csvData(csvData)
                            .build())
                    .execute())
                .saveCallback(dtoList -> personCsvImportSaveCmd.withRequest(
                        PersonCsvImportSaveCmd.Request.builder()
                            .dtoList(dtoList)
                            .build())
                    .execute())
                .build())
            .execute();

        return MessageResponse.builder()
            .code(HttpStatus.OK.value())
            .message("Persons imported successfully")
            .build();
    }

    @Operation(summary = "update person")
    @PutMapping("{personId}")
    @ResponseStatus(HttpStatus.OK)
    @CanUpdatePerson
    public SingleResponse<PersonResponse> updatePerson(@PathVariable("personId") UUID personId,
        @RequestBody PersonUpdateRequest request)
    {
        processPersonUpdateRequestCmd.withRequest(ProcessPersonUpdateRequestCmd.Request.builder()
                .personId(personId)
                .personUpdateRequest(request)
                .build())
            .execute();

        return SingleResponse.<PersonResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Person updated successfully")
            .build();
    }
}

