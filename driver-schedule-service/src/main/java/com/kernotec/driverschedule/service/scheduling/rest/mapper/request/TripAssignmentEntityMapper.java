package com.kernotec.driverschedule.service.scheduling.rest.mapper.request;

import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripAssignment;
import com.kernotec.driverschedule.service.scheduling.rest.dto.request.schedule.TripAssignmentCreateRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface TripAssignmentEntityMapper {

    TripAssignment toEntity(TripAssignmentCreateRequest request, UUID scheduleTransportationId,
        UUID tripAssignmentStateId);

    default List<TripAssignment> toEntity(List<TripAssignmentCreateRequest> requestList,
        UUID scheduleTransportationId, UUID tripAssignmentStateId)
    {
        if (requestList == null) {
            return null;
        }

        List<TripAssignment> list = new ArrayList<>(requestList.size());

        for (TripAssignmentCreateRequest tripAssignmentCreateRequest : requestList) {
            list.add(toEntity(
                tripAssignmentCreateRequest, scheduleTransportationId,
                tripAssignmentStateId
            ));
        }

        return list;
    }
}
