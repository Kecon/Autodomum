/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Kenny Colliander Nordin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.autodomum.core.holiday;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Abstract test class that is used by holidays to check that days are valid
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
public abstract class AbstractHolidayTest {
	protected void testHoliday(final Holiday holiday, final String... dates) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = GregorianCalendar.getInstance();

		for (final String date : dates) {
			calendar.setTime(dateFormat.parse(date));

			assertTrue(date, holiday.isHoliday(calendar));
			for (int i = 0; i < 50; i++) {
				calendar.add(Calendar.DATE, 1);
				assertFalse(date, holiday.isHoliday(calendar));
			}

			calendar.add(Calendar.DATE, -102);

			for (int i = 0; i < 50; i++) {
				calendar.add(Calendar.DATE, 1);
				assertFalse(date, holiday.isHoliday(calendar));
			}
		}
	}
}
