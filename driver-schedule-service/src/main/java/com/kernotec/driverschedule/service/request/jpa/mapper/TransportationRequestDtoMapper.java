package com.kernotec.driverschedule.service.request.jpa.mapper;

import com.kernotec.driverschedule.service.request.jpa.dto.TransportationRequestDto;
import com.kernotec.driverschedule.person.jpa.mapper.PersonDtoFlatMapper;
import com.kernotec.driverschedule.service.request.jpa.entity.TransportationRequest;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {PersonDtoFlatMapper.class})
public interface TransportationRequestDtoMapper {

    TransportationRequestDto toDto(TransportationRequest transportationRequest);

    List<TransportationRequestDto> toDto(List<TransportationRequest> transportationRequestList);

    Set<TransportationRequestDto> toDto(Set<TransportationRequest> transportationRequestSet);
}
