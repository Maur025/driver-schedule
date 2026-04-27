package com.kernotec.driverscheduleservice.command.trip.trip.log;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.trip.TripLog;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripLogService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripLogManyCreateCmd extends
    AbstractTransactionalRequiredCommand<TripLogManyCreateCmd.Request, List<TripLog>>
{

    private final TripLogService tripLogService;

    @Override
    protected List<TripLog> run(Request request) {
        if (request.tripLogList()
            .isEmpty())
        {
            return null;
        }

        return tripLogService.saveAll(request.tripLogList());
    }

    @Builder
    public record Request(@NotNull List<TripLog> tripLogList) {

    }
}