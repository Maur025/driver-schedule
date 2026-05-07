package com.kernotec.driverschedule.service.rest.socket.trip;

import com.kernotec.driverschedule.service.jpa.service.trip.TripEmergencySocketService;
import com.kernotec.driverschedule.service.rest.dto.trip.response.trip.emergency.TripEmergencyResponse;
import com.kernotec.driverschedule.service.rest.socket.SocketHandler;
import com.kernotec.driverschedule.service.web.socket.WebSocketHandler;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class TripEmergencySocketHandler extends
    SocketHandler<TripEmergencySocketHandler.Request, TripEmergencyResponse>
{

    private final TripEmergencySocketService tripEmergencySocketService;

    public TripEmergencySocketHandler(WebSocketHandler webSocketHandler,
        TripEmergencySocketService tripEmergencySocketService)
    {
        super(webSocketHandler);
        this.tripEmergencySocketService = tripEmergencySocketService;
    }

    @Override
    protected String getTopic(Request request) {
        return request.topic();
    }

    @Override
    protected Set<UUID> getToList(Request request) {
        return request.toList();
    }

    @Override
    protected TripEmergencyResponse getResponseData(Request request) {
        return tripEmergencySocketService.getResponseWithAllRelations(request.tripEmergencyId());
    }

    @Builder
    public record Request(@NotNull UUID tripEmergencyId, @NotNull String topic, Set<UUID> toList) {

    }
}
