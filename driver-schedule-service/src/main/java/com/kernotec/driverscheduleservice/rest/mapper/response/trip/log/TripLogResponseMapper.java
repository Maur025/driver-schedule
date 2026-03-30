package com.kernotec.driverscheduleservice.rest.mapper.response.trip.log;

import com.kernotec.driverscheduleservice.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverscheduleservice.jpa.entity.TripLog;
import com.kernotec.driverscheduleservice.rest.dto.response.trip.log.TripLogResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.trip.TripNAResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {TripNAResponseFlatMapper.class, AuthUserDataResponseMapper.class})
public interface TripLogResponseMapper {

    TripLogResponse toResponse(TripLog tripLog);

    TripLogResponse toResponse(UUID id);

    List<TripLogResponse> toResponse(List<TripLog> tripLogList);

    Set<TripLogResponse> toResponse(Set<TripLog> tripLogSet);
}
