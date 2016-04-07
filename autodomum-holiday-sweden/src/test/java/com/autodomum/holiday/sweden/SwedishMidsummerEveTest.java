package com.autodomum.holiday.sweden;

import java.text.ParseException;

import org.junit.Test;

public class SwedishMidsummerEveTest extends SwedishAbstractHolidayTest {

	@Test
	public void testIsHoliday() throws ParseException {
		this.testHoliday(new SwedishMidsummerEve(), "2016-06-24", "2017-06-23", "2018-06-22", "2019-06-21");
	}

}
