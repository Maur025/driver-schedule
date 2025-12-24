package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.enums.PersonTypeEnum;
import com.kernotec.driverscheduleservice.jpa.repository.TransportationRequestRepository;
import com.kernotec.driverscheduleservice.jpa.specification.transportation.request.TransportationRequestSpecification;
import com.kernotec.driverscheduleservice.rest.dto.request.transportation.request.TransportationRequestFilterRequest;
import com.kernotec.driverscheduleservice.util.AuthUtil;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TransportationRequestService extends BaseServiceImpl<TransportationRequest, UUID> {

    private final TransportationRequestRepository repository;
    private final AuthUtil authUtil;

    @Override
    protected String resourceName() {
        return "Transportation Request";
    }

    @Override
    protected BaseRepository<TransportationRequest, UUID> repository() {
        return repository;
    }

    public Page<TransportationRequest> findAllWithFilters(
        TransportationRequestFilterRequest filterRequest, Authentication authentication,
        Pageable pageable)
    {
        TransportationRequestSpecification transportationRequestSpecification = TransportationRequestSpecification.builder()
            .withTransportationRequestStateId(filterRequest.getTransportationRequestStateId())
            .withPersonRequestedId(filterRequest.getPersonRequestedId())
            .withTransportationRequestState(filterRequest.getTransportationRequestState())
            .withTripType(filterRequest.getTripType());

        boolean isAdmin = authUtil.userContainsRole(authentication, PersonTypeEnum.ADMIN);
        boolean isApplicant = authUtil.userContainsRole(authentication, PersonTypeEnum.APPLICANT);

        if (!isAdmin && isApplicant) {
            transportationRequestSpecification.withUserId(
                String.valueOf(authUtil.getUserIdFromAuthentication(authentication)));
        }

        return repository.findAll(transportationRequestSpecification, pageable);
    }
}
