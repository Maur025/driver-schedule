package com.kernotec.driverscheduleservice.command.trip.emergency.reason;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.trip.EmergencyReason;
import com.kernotec.driverscheduleservice.jpa.service.trip.EmergencyReasonService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmergencyReasonCreateCmd extends
    AbstractTransactionalRequiredCommand<EmergencyReasonCreateCmd.Request, UUID>
{

    private final EmergencyReasonService emergencyReasonService;

    @Override
    protected UUID run(Request request) {
        var emergencyReason = new EmergencyReason();

        emergencyReason.setReasonId(request.reasonId);
        emergencyReason.setTripEmergencyId(request.tripEmergencyId);
        emergencyReason.setOtherReason(request.otherReason);

        emergencyReason = emergencyReasonService.save(emergencyReason);
        return emergencyReason.getId();
    }

    @Builder
    public record Request(@NotNull UUID reasonId, @NotNull UUID tripEmergencyId,
                          String otherReason)
    {

    }
}
