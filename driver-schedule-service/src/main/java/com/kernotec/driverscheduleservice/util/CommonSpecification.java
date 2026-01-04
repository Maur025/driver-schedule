package com.kernotec.driverscheduleservice.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonSpecification {

    public static void queryOrderBy(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb,
        String orderBy, boolean isDescending)
    {
        if (orderBy == null) {
            log.debug("orderBy is null");
            return;
        }

        query.orderBy(isDescending ? cb.desc(root.get(orderBy)) : cb.asc(root.get(orderBy)));
    }

    public static Predicate simpleDatePredicate(CriteriaBuilder cb, Path<ZonedDateTime> datePath,
        ZonedDateTime simpleDateInput, String zoneId)
    {
        ZoneId clientZoneId = ZonedDateTimeUtil.getClientZoneId(zoneId);
        LocalDate simpleLocalDate = simpleDateInput.toLocalDate();

        ZonedDateTime startOfDay = simpleLocalDate.atStartOfDay(clientZoneId);

        ZonedDateTime endOfDay = simpleLocalDate.atTime(LocalTime.MAX)
            .atZone(clientZoneId);

        log.debug("startOfDay: {}, endOfDay: {}", startOfDay, endOfDay);

        return cb.between(datePath, startOfDay, endOfDay);
    }

    public static Predicate dateRangePredicate(CriteriaBuilder cb, Path<ZonedDateTime> datePath,
        ZonedDateTime fromDate, ZonedDateTime toDate, String zoneId)
    {
        ZoneId clientZoneId = ZonedDateTimeUtil.getClientZoneId(zoneId);

        LocalDate fromLocalDate = fromDate.toLocalDate();
        ZonedDateTime startOfFromDate = fromLocalDate.atStartOfDay(clientZoneId);

        LocalDate toLocalDate = toDate.toLocalDate();
        ZonedDateTime endOfToDate = toLocalDate.atTime(LocalTime.MAX)
            .atZone(clientZoneId);

        log.debug("startOfFromDate: {}, endOfToDate: {}", startOfFromDate, endOfToDate);

        return cb.between(datePath, startOfFromDate, endOfToDate);
    }

    public static Predicate monthDatePredicate(CriteriaBuilder cb, Path<ZonedDateTime> datePath,
        ZonedDateTime monthDateInput, String zoneId)
    {
        ZoneId clientZoneId = ZonedDateTimeUtil.getClientZoneId(zoneId);

        LocalDate monthLocalDate = monthDateInput.withDayOfMonth(1)
            .toLocalDate();

        ZonedDateTime startOfMonth = monthLocalDate.atStartOfDay(clientZoneId);

        ZonedDateTime endOfMonth = startOfMonth.plusMonths(1)
            .minusNanos(1);

        log.debug("startOfMonth: {}, endOfMonth: {}", startOfMonth, endOfMonth);

        return cb.between(datePath, startOfMonth, endOfMonth);
    }

    public static Predicate yearDatePredicate(CriteriaBuilder cb, Path<ZonedDateTime> datePath,
        ZonedDateTime yearDateInput, String zoneId)
    {
        ZoneId clientZoneId = ZonedDateTimeUtil.getClientZoneId(zoneId);

        LocalDate yearLocalDate = yearDateInput.withDayOfYear(1)
            .toLocalDate();

        ZonedDateTime startOfYear = yearLocalDate.atStartOfDay(clientZoneId);

        ZonedDateTime endOfYear = startOfYear.plusYears(1)
            .minusNanos(1);

        log.debug("startOfYear: {}, endOfYear: {}", startOfYear, endOfYear);

        return cb.between(datePath, startOfYear, endOfYear);
    }
}
