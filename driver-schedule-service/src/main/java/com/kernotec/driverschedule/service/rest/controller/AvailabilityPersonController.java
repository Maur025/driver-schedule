package com.kernotec.driverschedule.service.rest.controller;

import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.person.authorize.annotation.CanReadPerson;
import com.kernotec.driverschedule.person.exception.PersonException;
import com.kernotec.driverschedule.person.jpa.entity.Person;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
import com.kernotec.driverschedule.person.rest.ResourceApiSpec.PersonSpec;
import com.kernotec.driverschedule.service.jpa.service.resource.AvailabilityForAssignmentService;
import com.kernotec.driverschedule.service.rest.dto.resource.PersonScheduleConflictRequest;
import com.kernotec.driverschedule.service.rest.dto.resource.request.AvailabilityForAssignmentRequest;
import com.kernotec.driverschedule.service.rest.dto.resource.response.AvailabilityForAssignmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = PersonSpec.TAG_NAME, description = PersonSpec.TAG_DESCRIPTION)
@RequestMapping(path = PersonSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class AvailabilityPersonController {

    private final PersonService personService;
    private final AvailabilityForAssignmentService availabilityForAssignmentService;

    @Operation(summary = "find driver schedule conflicts")
    @PostMapping("{driverId}/schedule-conflicts")
    @ResponseStatus(HttpStatus.OK)
    @CanReadPerson
    public SingleResponse<AvailabilityForAssignmentResponse> findPersonScheduleConflicts(
        @PathVariable("driverId") UUID driverId, @RequestBody PersonScheduleConflictRequest request)
    {
        List<Person> personNotUsableList = personService.canNotBeUsedAsDriver(Set.of(driverId));

        if (!personNotUsableList.isEmpty()) {
            throw new PersonException(
                "not.usable", "'" + driverId + "'", HttpStatus.CONFLICT.value());
        }

        AvailabilityForAssignmentResponse availabilityForAssignmentResponse = availabilityForAssignmentService.checkDriverIsAvailable(
            AvailabilityForAssignmentRequest.builder()
                .driverIds(List.of(driverId))
                .dateFrom(request.getConflictValidationFrom())
                .dateTo(request.getConflictValidationTo())
                .zoneId(request.getZoneId())
                .scheduleTransportationExcludeId(request.getScheduleTransportationExcludeId())
                .build());

        return SingleResponse.<AvailabilityForAssignmentResponse>builder()
            .code(HttpStatus.OK.value())
            .data(availabilityForAssignmentResponse)
            .build();
    }
}
