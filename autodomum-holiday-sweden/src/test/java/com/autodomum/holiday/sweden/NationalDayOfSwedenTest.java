package com.autodomum.holiday.sweden;

import java.text.ParseException;

import org.junit.Test;

public class NationalDayOfSwedenTest extends SwedishAbstractHolidayTest {

	@Test
	public void testIsHoliday() throws ParseException {
		this.testHoliday(new NationalDayOfSweden(), "2016-06-06", "2017-06-06", "2018-06-06", "2019-06-06");
	}

}
