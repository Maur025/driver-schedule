package com.kernotec.driverscheduleservice.rest.socket.trip;

import com.kernotec.driverscheduleservice.jpa.entity.trip.Trip;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripService;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.TripResponse;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.TripResponseMapper;
import com.kernotec.driverscheduleservice.rest.socket.SocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class TripSocketHandler extends SocketHandler<TripSocketHandler.Request, TripResponse> {

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
