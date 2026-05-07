package com.kernotec.driverschedule.service.request.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.exception.request.TransportationRequestStateException;
import com.kernotec.driverschedule.service.request.jpa.entity.TransportationRequestState;
import com.kernotec.driverschedule.service.request.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverschedule.service.request.jpa.repository.TransportationRequestStateRepository;
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
