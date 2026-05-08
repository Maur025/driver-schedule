package com.kernotec.driverschedule.service.rest.socket.trip;

import com.kernotec.driverschedule.service.jpa.entity.trip.Trip;
import com.kernotec.driverschedule.service.jpa.service.trip.TripService;
import com.kernotec.driverschedule.service.rest.dto.trip.response.trip.TripResponse;
import com.kernotec.driverschedule.service.rest.mapper.trip.response.trip.TripResponseMapper;
import com.kernotec.driverschedule.service.rest.socket.trip.TripSocketHandler.Request;
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
