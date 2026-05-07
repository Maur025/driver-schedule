package com.kernotec.driverschedule.service.request.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.command.request.reject.reason.RejectReasonCreateCmd;
import com.kernotec.driverschedule.service.command.request.transportation.request.TransportationRequestGetDtoCmd;
import com.kernotec.driverschedule.service.command.request.transportation.request.TransportationRequestUpdateCmd;
import com.kernotec.driverschedule.service.command.request.transportation.request.log.TransportationRequestLogCreateCmd;
import com.kernotec.driverschedule.service.exception.request.TransportationRequestException;
import com.kernotec.driverschedule.service.request.jpa.dto.TransportationRequestDto;
import com.kernotec.driverschedule.service.request.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverschedule.service.request.jpa.service.TransportationRequestStateService;
import com.kernotec.driverschedule.service.request.rest.dto.request.RejectReasonRequest;
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
    private final RejectReasonCreateCmd rejectReasonCreateCmd;
    private final TransportationRequestLogCreateCmd transportationRequestLogCreateCmd;

    @Override
    protected void validate(Request request) {
        TransportationRequestDto transportationRequestDto = transportationRequestGetDtoCmd.withRequest(
                TransportationRequestGetDtoCmd.Request.builder()
                    .transportationRequestId(request.transportationRequestId)
                    .build())
            .execute();

        TransportationRequestStateEnum requestStateCurrent = TransportationRequestStateEnum.fromValue(
            transportationRequestDto.getTransportationRequestState()
                .getCode());

        if (!requestStateCurrent.canTransitionTo(TransportationRequestStateEnum.REJECTED)) {
            throw new TransportationRequestException(
                "state.invalid.to.reject",
                "'" + requestStateCurrent + "'", HttpStatus.BAD_REQUEST.value()
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

        rejectReasonCreateCmd.withRequest(RejectReasonCreateCmd.Request.builder()
                .transportationRequestId(request.transportationRequestId)
                .reasonId(rejectReasonRequest.getReasonId())
                .otherReason(rejectReasonRequest.getOtherReason())
                .build())
            .execute();

        transportationRequestLogCreateCmd.withRequest(
                TransportationRequestLogCreateCmd.Request.builder()
                    .transportationRequestId(request.transportationRequestId())
                    .transportationRequestStateId(transportationRequestStateRejectedId)
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
