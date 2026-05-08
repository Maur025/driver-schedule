package com.kernotec.driverschedule.service.command.request.transportation.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.request.jpa.dto.TransportationRequestDto;
import com.kernotec.driverschedule.service.request.jpa.mapper.TransportationRequestDtoMapper;
import com.kernotec.driverschedule.service.request.jpa.entity.TransportationRequest;
import com.kernotec.driverschedule.service.request.jpa.service.TransportationRequestService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransportationRequestGetDtoCmd extends
    AbstractTransactionalRequiredCommand<TransportationRequestGetDtoCmd.Request, TransportationRequestDto>
{

    private final TransportationRequestService transportationRequestService;
    private final TransportationRequestDtoMapper transportationRequestDtoMapper;

    @Override
    protected TransportationRequestDto run(Request request) {
        TransportationRequest transportationRequest = transportationRequestService.findByIdThrow(
            request.transportationRequestId);
        return transportationRequestDtoMapper.toDto(transportationRequest);
    }

    @Builder
    public record Request(@NotNull UUID transportationRequestId) {

    }
}
