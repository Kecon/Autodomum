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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

/**
 * Collecting country holidays, etc and validate all with a single method
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
public class HolidayCollection implements Holiday {
	private final Collection<Holiday> holidays;

	protected HolidayCollection(final Holiday... holidays) {
		if (holidays == null) {
			throw new NullPointerException("Parameter holidays may not be null");
		}
		this.holidays = Arrays.asList(holidays);
	}

	@Override
	public boolean isHoliday(final Calendar calendar) {
		for (final Holiday holiday : this.holidays) {
			if (holiday.isHoliday(calendar)) {
				return true;
			}
		}

		return false;
	}
}
