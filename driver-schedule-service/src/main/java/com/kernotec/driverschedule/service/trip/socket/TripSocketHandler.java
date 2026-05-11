package com.kernotec.driverschedule.service.trip.socket;

import com.kernotec.driverschedule.service.trip.jpa.entity.Trip;
import com.kernotec.driverschedule.service.trip.jpa.service.TripService;
import com.kernotec.driverschedule.service.trip.rest.dto.response.TripResponse;
import com.kernotec.driverschedule.service.trip.rest.mapper.response.TripResponseMapper;
import com.kernotec.driverschedule.service.trip.socket.TripSocketHandler.Request;
import com.kernotec.driverschedule.socket.service.SocketHandler;
import com.kernotec.driverschedule.socket.service.WebSocketHandler;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class TripSocketHandler extends SocketHandler<Request, TripResponse> {

    private final TripService tripService;
    private final TripResponseMapper tripResponseMapper;

    public TripSocketHandler(WebSocketHandler webSocketHandler, TripService tripService,
        TripResponseMapper tripResponseMapper)
    {
        super(webSocketHandler);
        this.tripService = tripService;
        this.tripResponseMapper = tripResponseMapper;
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
    protected TripResponse getResponseData(Request request) {
        Trip trip = tripService.findByIdThrow(request.tripId());
        return tripResponseMapper.toResponse(trip);
    }

    @Builder
    public record Request(@NotNull UUID tripId, @NotNull String topic, Set<UUID> toList) {

    }
}
