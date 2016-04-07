package com.autodomum.holiday.sweden;

import java.text.ParseException;

import org.junit.Test;

public class SwedishAllSaintsDayTest extends SwedishAbstractHolidayTest {

	@Test
	public void testIsHoliday() throws ParseException {
		this.testHoliday(new SwedishAllSaintsDay(), "2016-11-05", "2017-11-04", "2018-11-03", "2019-11-02");
	}

}
