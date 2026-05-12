package com.kernotec.driverschedule.service.trip.socket;

import com.kernotec.driverschedule.service.trip.jpa.service.TripEmergencySocketService;
import com.kernotec.driverschedule.service.trip.rest.dto.response.TripEmergencyResponse;
import com.kernotec.driverschedule.service.trip.socket.TripEmergencySocketHandler.Request;
import com.kernotec.driverschedule.socket.service.SocketHandler;
import com.kernotec.driverschedule.socket.service.WebSocketHandler;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class TripEmergencySocketHandler extends SocketHandler<Request, TripEmergencyResponse> {

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
