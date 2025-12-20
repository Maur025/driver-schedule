package com.kernotec.driverscheduleservice.rest.command.transportation.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.reason.ReasonCreateCmd;
import com.kernotec.driverscheduleservice.command.reject.reason.RejectReasonCreateCmd;
import com.kernotec.driverscheduleservice.command.transportation.request.TransportationRequestGetDtoCmd;
import com.kernotec.driverscheduleservice.command.transportation.request.TransportationRequestUpdateCmd;
import com.kernotec.driverscheduleservice.exception.TransportationRequestException;
import com.kernotec.driverscheduleservice.jpa.dto.TransportationRequestDto;
import com.kernotec.driverscheduleservice.jpa.dto.TransportationRequestStateDto;
import com.kernotec.driverscheduleservice.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.TransportationRequestStateService;
import com.kernotec.driverscheduleservice.rest.dto.request.reject.reason.RejectReasonRequest;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransportationRequestRejectedCmd extends
    AbstractTransactionalRequiredCommand<TransportationRequestRejectedCmd.Request, Void>
{

    private final TransportationRequestGetDtoCmd transportationRequestGetDtoCmd;
    private final TransportationRequestStateService transportationRequestStateService;
    private final TransportationRequestUpdateCmd transportationRequestUpdateCmd;
    private final ReasonCreateCmd reasonCreateCmd;
    private final RejectReasonCreateCmd rejectReasonCreateCmd;

    @Override
    protected void validate(Request request) {
        TransportationRequestDto transportationRequestDto = transportationRequestGetDtoCmd.withRequest(
                TransportationRequestGetDtoCmd.Request.builder()
                    .transportationRequestId(request.transportationRequestId)
                    .build())
            .execute();

        TransportationRequestStateDto transportationRequestStateDto = transportationRequestDto.getTransportationRequestState();

        if (!transportationRequestStateDto.getCode()
            .equals(String.valueOf(TransportationRequestStateEnum.REQUESTED)))
        {
            throw new TransportationRequestException(
                "state.invalid.to.reject",
                "'" + transportationRequestStateDto.getCode() + "'", HttpStatus.BAD_REQUEST.value()
            );
        }
    }

    @Override
    protected Void run(Request request) {
        RejectReasonRequest rejectReasonRequest = request.rejectReasonRequest;

        UUID transportationRequestStateRejectedId = transportationRequestStateService.findIdByCodeThrow(
            TransportationRequestStateEnum.REJECTED);

        transportationRequestUpdateCmd.withRequest(TransportationRequestUpdateCmd.Request.builder()
                .transportationRequestId(request.transportationRequestId)
                .transportationRequestStateId(transportationRequestStateRejectedId)
                .build())
            .execute();

        if (rejectReasonRequest.getReasonDescription() == null
            || rejectReasonRequest.getReasonDescription()
            .isBlank())
        {
            log.debug("No reject reason description provided, skipping reason creation.");
            return null;
        }

        UUID reasonId = reasonCreateCmd.withRequest(ReasonCreateCmd.Request.builder()
                .reasonDescription(rejectReasonRequest.getReasonDescription())
                .build())
            .execute();

        rejectReasonCreateCmd.withRequest(RejectReasonCreateCmd.Request.builder()
                .transportationRequestId(request.transportationRequestId)
                .reasonId(reasonId)
                .build())
            .execute();

        return null;
    }

    @Builder
    public record Request(@NotNull UUID transportationRequestId,
                          @NotNull RejectReasonRequest rejectReasonRequest)
    {

    }
}
