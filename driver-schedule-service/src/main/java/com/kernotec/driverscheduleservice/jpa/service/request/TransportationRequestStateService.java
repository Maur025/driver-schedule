package com.kernotec.driverscheduleservice.jpa.service.request;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.exception.request.TransportationRequestStateException;
import com.kernotec.driverscheduleservice.jpa.entity.request.TransportationRequestState;
import com.kernotec.driverscheduleservice.jpa.enums.request.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.jpa.repository.request.TransportationRequestStateRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TransportationRequestStateService extends
    BaseServiceImpl<TransportationRequestState, UUID>
{

    private final TransportationRequestStateRepository repository;

    @Override
    protected String resourceName() {
        return "Transportation Request State";
    }

    @Override
    protected BaseRepository<TransportationRequestState, UUID> repository() {
        return repository;
    }

    public Optional<TransportationRequestState> findByCode(TransportationRequestStateEnum code) {
        return repository.findByCode(code.toString());
    }

    public TransportationRequestState findByCodeThrow(TransportationRequestStateEnum code) {
        return findByCode(code).orElseThrow(
            () -> new TransportationRequestStateException(
                "code.not.found", "'" + code + "'",
                HttpStatus.NOT_FOUND.value()
            ));
    }

    public UUID findIdByCodeThrow(TransportationRequestStateEnum code) {
        return findByCodeThrow(code).getId();
    }
}
