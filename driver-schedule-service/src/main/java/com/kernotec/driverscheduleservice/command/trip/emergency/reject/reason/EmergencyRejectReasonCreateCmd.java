package com.kernotec.driverscheduleservice.command.trip.emergency.reject.reason;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.trip.EmergencyRejectReason;
import com.kernotec.driverscheduleservice.jpa.service.trip.EmergencyRejectReasonService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmergencyRejectReasonCreateCmd extends
    AbstractTransactionalRequiredCommand<EmergencyRejectReasonCreateCmd.Request, UUID>
{

    private final EmergencyRejectReasonService emergencyRejectReasonService;

    @Override
    protected UUID run(Request request) {
        var emergencyRejectReason = new EmergencyRejectReason();

        emergencyRejectReason.setOtherReason(request.otherReason());
        emergencyRejectReason.setReasonId(request.reasonId());
        emergencyRejectReason.setTripEmergencyId(request.tripEmergencyId());

        emergencyRejectReason = emergencyRejectReasonService.save(emergencyRejectReason);
        return emergencyRejectReason.getId();
    }

    @Builder
    public record Request(String otherReason, @NotNull UUID reasonId,
                          @NotNull UUID tripEmergencyId)
    {

    }
}