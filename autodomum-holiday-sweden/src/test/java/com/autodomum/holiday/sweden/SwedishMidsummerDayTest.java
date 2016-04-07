package com.autodomum.holiday.sweden;

import java.text.ParseException;

import org.junit.Test;

public class SwedishMidsummerDayTest extends SwedishAbstractHolidayTest {

	@Test
	public void testIsHoliday() throws ParseException {
		this.testHoliday(new SwedishMidsummerDay(), "2016-06-25", "2017-06-24", "2018-06-23", "2019-06-22");
	}

}
