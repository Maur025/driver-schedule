package com.kernotec.driverschedule.service.scheduling.rest.mapper.response;

import com.kernotec.driverschedule.service.scheduling.jpa.entity.TransportationRequest;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.TransportationRequestResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TransportationRequestToCurrentMapper {

    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "peopleNumber", ignore = true)
    @Mapping(target = "assets", ignore = true)
    @Mapping(target = "passengers", ignore = true)
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "requestedDate", ignore = true)
    @Mapping(target = "requestedFrom", ignore = true)
    @Mapping(target = "requestedTo", ignore = true)
    @Mapping(target = "tripType", ignore = true)
    @Mapping(target = "shortNotice", ignore = true)
    @Mapping(target = "detail", ignore = true)
    @Mapping(target = "assetPickup", ignore = true)
    @Mapping(target = "estimatedTotalDistanceKm", ignore = true)
    @Mapping(target = "estimatedTotalDurationMin", ignore = true)
    @Mapping(target = "wasRequestedByScheduler", ignore = true)
    @Mapping(target = "transportationRequestStateId", ignore = true)
    @Mapping(target = "transportationRequestState", ignore = true)
    @Mapping(target = "personRequestedId", ignore = true)
    @Mapping(target = "personRequested", ignore = true)
    @Mapping(target = "scheduleTransportations", ignore = true)
    @Mapping(target = "rejectReasons", ignore = true)
    @Mapping(target = "cancelReasons", ignore = true)
    @Mapping(target = "requestCoords", ignore = true)
    TransportationRequestResponse toResponse(TransportationRequest transportationRequest);

    List<TransportationRequestResponse> toResponse(
        List<TransportationRequest> transportationRequestList);

    Set<TransportationRequestResponse> toResponse(
        Set<TransportationRequest> transportationRequestSet);
}
