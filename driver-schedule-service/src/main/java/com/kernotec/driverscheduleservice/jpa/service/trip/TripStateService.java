package com.kernotec.driverscheduleservice.jpa.service.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.exception.trip.TripStateException;
import com.kernotec.driverscheduleservice.jpa.entity.trip.TripState;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverscheduleservice.jpa.repository.trip.TripStateRepository;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.state.TripStateLookupResponse;
import com.kernotec.driverscheduleservice.util.CommonUtil;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TripStateService extends BaseServiceImpl<TripState, UUID> {

    private final TripStateRepository repository;

    @Override
    protected String resourceName() {
        return "Trip State";
    }

    @Override
    protected BaseRepository<TripState, UUID> repository() {
        return repository;
    }

    public Page<TripStateLookupResponse> findAllToLookup(String keyword, Pageable pageable) {
        String keywordStr = CommonUtil.getSafeString(keyword);
        return repository.findAllToLookup(keywordStr, pageable);
    }

    public Optional<TripState> findByCode(TripStateEnum code) {
        return repository.findByCode(String.valueOf(code));
    }

    public TripState findByCodeThrow(TripStateEnum code) {
        return findByCode(code).orElseThrow(
            () -> new TripStateException(
                "code.not.found", "'" + code + "'",
                HttpStatus.NOT_FOUND.value()
            ));
    }

    public UUID findIdByCodeThrow(TripStateEnum code) {
        return findByCodeThrow(code).getId();
    }
}
