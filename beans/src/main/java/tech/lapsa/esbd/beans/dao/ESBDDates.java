package tech.lapsa.esbd.beans.dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import tech.lapsa.java.commons.function.MyStrings;

public final class ESBDDates {

	private ESBDDates() {
	}

	private static final String ESBD_DATE_FORMAT_PATTERN = "dd.MM.yyyy";

	private static final DateTimeFormatter DITE_TIME_FORMATTER = new DateTimeFormatterBuilder()
	        .appendPattern(ESBD_DATE_FORMAT_PATTERN).toFormatter();

	public static LocalDate convertESBDDateToLocalDate(final String esbdDate) {
		if (MyStrings.empty(esbdDate))
			return null;
		return LocalDate.parse(esbdDate, DITE_TIME_FORMATTER);
	}

	public static String convertLocalDateToESBDDate(final LocalDate localDate) {
		if (localDate == null)
			return null;
		return localDate.format(DITE_TIME_FORMATTER);
	}

	public static LocalDate fromESBDYearMonth(final String yearS, final int month) {
		final int year = Integer.parseInt(yearS);
		return LocalDate.of(year, month, 1);
	}

}
