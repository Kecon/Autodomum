package com.autodomum.core.holiday;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

public class HolidayCollectionTest {

	Holiday christmasEve;
	Holiday newYearsEve;

	HolidayCollection holidayCollection;

	@Before
	public void before() {

		christmasEve = new ChristmasDay();
		newYearsEve = new NewYearsDay();

		this.holidayCollection = new HolidayCollection(christmasEve, newYearsEve);
	}

	@Test
	public void test() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = GregorianCalendar.getInstance();

		calendar.setTime(dateFormat.parse("2016-12-25"));
		assertTrue(this.holidayCollection.isHoliday(calendar));
		calendar.add(Calendar.DATE, 1);
		assertFalse(this.holidayCollection.isHoliday(calendar));

		calendar.setTime(dateFormat.parse("2017-01-01"));
		assertTrue(this.holidayCollection.isHoliday(calendar));
		calendar.add(Calendar.DATE, 1);
		assertFalse(this.holidayCollection.isHoliday(calendar));
	}

}
