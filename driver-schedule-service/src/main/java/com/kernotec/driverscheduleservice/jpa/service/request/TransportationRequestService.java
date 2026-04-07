package com.kernotec.driverscheduleservice.jpa.service.request;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.common.security.SecurityAuthProvider;
import com.kernotec.driverscheduleservice.jpa.entity.request.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.enums.resource.PersonTypeEnum;
import com.kernotec.driverscheduleservice.jpa.repository.request.TransportationRequestRepository;
import com.kernotec.driverscheduleservice.jpa.service.resource.PersonService;
import com.kernotec.driverscheduleservice.jpa.specification.request.TransportationRequestSpecification;
import com.kernotec.driverscheduleservice.rest.dto.request.request.transportation.request.TransportationRequestFilterRequest;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TransportationRequestService extends BaseServiceImpl<TransportationRequest, UUID> {

    private final TransportationRequestRepository repository;
    private final SecurityAuthProvider securityAuthProvider;
    private final PersonService personService;

    @Override
    protected String resourceName() {
        return "Transportation Request";
    }

    @Override
    protected BaseRepository<TransportationRequest, UUID> repository() {
        return repository;
    }

    public Page<TransportationRequest> findAllWithFilters(
        TransportationRequestFilterRequest filterRequest, Pageable pageable)
    {
        boolean hasOnlyOneRole = securityAuthProvider.hasOnlyOneRole();
        boolean isApplicant = securityAuthProvider.userContainsRole(PersonTypeEnum.APPLICANT);

        UUID onlyRecordsOfPersonId = null;

        if (hasOnlyOneRole && isApplicant) {
            onlyRecordsOfPersonId = personService.findIdByUserIdAuthenticateThrow();
        }

        return repository.findAll(
            TransportationRequestSpecification.builder()
                .withTransportationRequestStateId(filterRequest.getTransportationRequestStateId())
                .withPersonRequestedId(filterRequest.getPersonRequestedId())
                .withTransportationRequestState(filterRequest.getTransportationRequestState())
                .withTripType(filterRequest.getTripType())
                .withZoneId(filterRequest.getZoneId())
                .withSimpleDate(filterRequest.getSimpleDate())
                .withDateRange(filterRequest.getFromDate(), filterRequest.getToDate())
                .withMonthDate(filterRequest.getMonthDate())
                .withYearDate(filterRequest.getYearDate())
                .withKeyword(filterRequest.getKeyword())
                .withOnlyRecordsOfPersonId(onlyRecordsOfPersonId), pageable
        );
    }
}
