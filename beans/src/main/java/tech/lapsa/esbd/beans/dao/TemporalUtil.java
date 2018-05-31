package tech.lapsa.esbd.beans.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.OptionalInt;

import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;

public final class TemporalUtil {

    private TemporalUtil() {
    }

    public static final String ESBD_DATE_FORMAT = "dd.MM.yyyy";
    public static final String ESBD_DATETIME_FORMAT = "dd.MM.yyyy H:mm:ss";

    public static final String ESBD_ZONE_ID = "Asia/Almaty";
    public static final ZoneId ESBD_ZONE = ZoneId.of(ESBD_ZONE_ID);

    public static final DateTimeFormatter ESBD_DATE_FORMATTER = new DateTimeFormatterBuilder()
	    .appendPattern(ESBD_DATE_FORMAT).toFormatter();

    public static final DateTimeFormatter ESBD_DATETIME_FORMATTER = new DateTimeFormatterBuilder()
	    .appendPattern(ESBD_DATETIME_FORMAT).toFormatter();

    // from esbd to java

    public static LocalDate dateToLocalDate(final String esbdDate) {
	if (MyStrings.empty(esbdDate))
	    return null;
	return LocalDate.parse(esbdDate, ESBD_DATE_FORMATTER);
    }

    public static LocalDateTime datetimeToLocalDateTime(final String esbdDatetime) {
	if (MyStrings.empty(esbdDatetime))
	    return null;
	return LocalDateTime.parse(esbdDatetime, ESBD_DATETIME_FORMATTER);
    }

    public static Instant dateToInstant(final String esbdDate) {
	final LocalDate ld = dateToLocalDate(esbdDate);
	return MyObjects.nullOrGet(ld, x -> x.atStartOfDay(ESBD_ZONE).toInstant());
    }

    public static Instant datetimeToInstant(final String esbdDatetime) {
	final LocalDateTime ldt = datetimeToLocalDateTime(esbdDatetime);
	return MyObjects.nullOrGet(ldt, x -> x.atZone(ESBD_ZONE).toInstant());
    }

    public static Optional<Instant> optTemporalToInstant(final String esbdTemporal) {
	Instant res = null;
	try {
	    res = datetimeToInstant(esbdTemporal);
	} catch (DateTimeParseException e) {
	    try {
		res = dateToInstant(esbdTemporal);
	    } catch (DateTimeParseException e1) {
		res = null;
	    }
	}
	return MyOptionals.of(res);
    }

    public static LocalDate yearMonthToLocalDate(final String yearS, final int month) {

	final OptionalInt optYear;

	{
	    final Optional<Integer> temp = MyOptionals.of(yearS)
		    .map(Integer::parseInt)
		    .filter(MyNumbers::positive);
	    optYear = temp.isPresent()
		    ? OptionalInt.of(temp.get())
		    : OptionalInt.empty();
	}

	final OptionalInt optMonth;

	{
	    final Optional<Integer> temp = MyOptionals.of(month)
		    .filter(MyNumbers::positive)
		    .filter(x -> x <= 12);
	    optMonth = temp.isPresent()
		    ? OptionalInt.of(temp.get())
		    : OptionalInt.empty();
	}

	return optYear.isPresent()
		? LocalDate.of(optYear.getAsInt(), optMonth.orElse(1), 1)
		: null;
    }

    public static Optional<LocalDate> optYearMonthToLocalDate(final String yearS, final int month) {
	return MyOptionals.of(yearMonthToLocalDate(yearS, month));
    }

    // from java to esbd

    public static String localDateToDate(final LocalDate localDate) {
	if (localDate == null)
	    return null;
	return localDate.format(ESBD_DATE_FORMATTER);
    }

    public static String instantToDate(final Instant instant) {
	if (instant == null)
	    return null;
	return ESBD_DATE_FORMATTER.format(instant);
    }

    public static String instantToDateTime(final Instant instant) {
	if (instant == null)
	    return null;
	return ESBD_DATETIME_FORMATTER.format(instant);
    }
}
