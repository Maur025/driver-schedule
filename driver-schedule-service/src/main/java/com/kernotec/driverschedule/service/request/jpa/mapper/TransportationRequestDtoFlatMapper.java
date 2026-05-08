package com.kernotec.driverschedule.service.request.jpa.mapper;

import com.kernotec.driverschedule.service.request.jpa.dto.TransportationRequestDto;
import com.kernotec.driverschedule.service.request.jpa.entity.TransportationRequest;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TransportationRequestDtoFlatMapper {

    @Mapping(target = "transportationRequestState", ignore = true)
    @Mapping(target = "personRequested", ignore = true)
    @Mapping(target = "rejectReasons", ignore = true)
    TransportationRequestDto toDto(TransportationRequest transportationRequest);

    List<TransportationRequestDto> toDto(List<TransportationRequest> transportationRequestList);

    Set<TransportationRequestDto> toDto(Set<TransportationRequest> transportationRequestSet);
}
