package com.kernotec.driverscheduleservice.request.jpa.mapper;

import com.kernotec.driverscheduleservice.request.jpa.dto.TransportationRequestDto;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.resource.PersonDtoFlatMapper;
import com.kernotec.driverscheduleservice.request.jpa.entity.TransportationRequest;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {PersonDtoFlatMapper.class})
public interface TransportationRequestDtoMapper {

    TransportationRequestDto toDto(TransportationRequest transportationRequest);

    List<TransportationRequestDto> toDto(List<TransportationRequest> transportationRequestList);

    Set<TransportationRequestDto> toDto(Set<TransportationRequest> transportationRequestSet);
}
