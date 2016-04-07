package com.autodomum.holiday.sweden;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.autodomum.core.holiday.Holiday;

public abstract class SwedishAbstractHolidayTest {
	protected void testHoliday(final Holiday holiday, String... dates) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = GregorianCalendar.getInstance();

		for (final String date : dates) {
			calendar.setTime(dateFormat.parse(date));

			assertTrue(holiday.isHoliday(calendar));
			for (int i = 0; i < 160; i++) {
				calendar.add(Calendar.DATE, 1);
				assertFalse(holiday.isHoliday(calendar));
			}

			calendar.add(Calendar.DATE, -322);

			for (int i = 0; i < 160; i++) {
				calendar.add(Calendar.DATE, 1);
				assertFalse(holiday.isHoliday(calendar));
			}
		}
	}
}
