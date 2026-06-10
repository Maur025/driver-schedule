package com.kernotec.driverschedule.service.scheduling.rest.dto.request.schedule;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;
import lombok.Builder;


@Builder
public record AvailabilityForAssignmentRequest(ZonedDateTime dateFrom, ZonedDateTime dateTo,
                                               String zoneId, UUID scheduleTransportationExcludeId,
                                               Collection<UUID> driverIds,
                                               Collection<UUID> vehicleIds)
{

}
