package com.kernotec.driverschedule.service.scheduling.command.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.RequestCoord;
import com.kernotec.driverschedule.service.scheduling.jpa.service.RequestCoordService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor

@Service
public class RequestCoordManyCreateCmd extends
    AbstractTransactionalRequiredCommand<RequestCoordManyCreateCmd.Request, List<RequestCoord>>
{

    private final RequestCoordService requestCoordService;

    @Override
    protected List<RequestCoord> run(Request request) {
        if (request.requestCoordList.isEmpty()) {
            log.debug("RequestCoordManyCreateCmd: No coordinates to create.");
            return null;
        }

        return requestCoordService.saveAll(request.requestCoordList);
    }

    @Builder
    public record Request(@NotNull List<RequestCoord> requestCoordList) {

    }
}
