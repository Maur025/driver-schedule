package com.kernotec.driverschedule.service.scheduling.command.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.EmergencyReason;
import com.kernotec.driverschedule.service.scheduling.jpa.service.EmergencyReasonService;
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
