package com.kernotec.driverscheduleservice.rest.mapper.request.trip.assignment;

import com.kernotec.driverscheduleservice.jpa.entity.TripAssignment;
import com.kernotec.driverscheduleservice.rest.dto.request.trip.assignment.TripAssignmentCreateRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface TripAssignmentEntityMapper {

    TripAssignment toEntity(TripAssignmentCreateRequest request, UUID scheduleTransportationId);

    default List<TripAssignment> toEntity(List<TripAssignmentCreateRequest> requestList,
        UUID scheduleTransportationId)
    {
        if (requestList == null) {
            return null;
        }

        List<TripAssignment> list = new ArrayList<>(requestList.size());

        for (TripAssignmentCreateRequest tripAssignmentCreateRequest : requestList) {
            list.add(toEntity(tripAssignmentCreateRequest, scheduleTransportationId));
        }

        return list;
    }
}
