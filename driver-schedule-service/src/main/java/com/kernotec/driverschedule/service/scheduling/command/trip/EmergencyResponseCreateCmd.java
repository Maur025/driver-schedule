package com.kernotec.driverschedule.service.scheduling.command.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.EmergencyResponse;
import com.kernotec.driverschedule.service.scheduling.jpa.service.EmergencyResponseService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmergencyResponseCreateCmd extends
    AbstractTransactionalRequiredCommand<EmergencyResponseCreateCmd.Request, UUID>
{

    private final EmergencyResponseService emergencyResponseService;

    @Override
    protected UUID run(Request request) {
        var emergencyResponse = new EmergencyResponse();

        emergencyResponse.setTripEmergencyId(request.tripEmergencyId);
        emergencyResponse.setEmergencyResponseTypeId(request.emergencyResponseTypeId);
        emergencyResponse.setDetail(request.detail);

        emergencyResponse = emergencyResponseService.save(emergencyResponse);
        return emergencyResponse.getId();
    }

    @Builder
    public record Request(@NotNull UUID tripEmergencyId, @NotNull UUID emergencyResponseTypeId,
                          String otherResponseDetail, String detail)
    {

    }
}