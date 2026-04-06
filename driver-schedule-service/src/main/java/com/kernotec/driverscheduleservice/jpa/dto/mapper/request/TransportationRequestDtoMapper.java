package com.kernotec.driverscheduleservice.jpa.dto.mapper.request;

import com.kernotec.driverscheduleservice.jpa.dto.request.TransportationRequestDto;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.resource.PersonDtoFlatMapper;
import com.kernotec.driverscheduleservice.jpa.entity.request.TransportationRequest;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {PersonDtoFlatMapper.class})
public interface TransportationRequestDtoMapper {

    TransportationRequestDto toDto(TransportationRequest transportationRequest);

    List<TransportationRequestDto> toDto(List<TransportationRequest> transportationRequestList);

    Set<TransportationRequestDto> toDto(Set<TransportationRequest> transportationRequestSet);
}
