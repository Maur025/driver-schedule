package com.kernotec.driverschedule.common.datetime;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Converter(autoApply = true)
public class SafeZoneDateTimeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }

        return Timestamp.from(zonedDateTime.withZoneSameInstant(ZoneOffset.UTC)
            .toInstant());
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }

        return ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.UTC);
    }
}
