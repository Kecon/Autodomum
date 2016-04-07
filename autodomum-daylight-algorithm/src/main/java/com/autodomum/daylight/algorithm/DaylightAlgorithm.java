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
package com.autodomum.daylight.algorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.autodomum.core.daylight.Coordinate;
import com.autodomum.core.daylight.Daylight;

import static java.lang.Math.*;

/**
 * Keep track of daylight. Originally from:
 * "A Model Comparison for Daylength as a function of latitude and day of the year"
 * , Ecological Modelling, volume 80 (1995)
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
@Component
@Scope
public class DaylightAlgorithm implements Daylight {

	/**
	 * Calculate length of the day for a specific day
	 * 
	 * @param latitude
	 *            the latitude
	 * @param day
	 *            the day
	 * @return time in hours
	 */
	public double length(double latitude, int day) {

		final double p = Math
				.asin(.39795 * Math.cos(.2163108 + 2 * Math.atan(.9671396 * Math.tan(.00860 * (day - 186)))));

		return (24.0d - (24.0d / Math.PI)
				* Math.acos((Math.sin(0.8333d * Math.PI / 180d) + Math.sin(latitude * Math.PI / 180.0d) * Math.sin(p))
						/ (Math.cos(latitude * Math.PI / 180.0d) * Math.cos(p))));
	}

	public double localSolarTime(int day) {
		final double b = (360d / 365d) * (day - 81);

		return 9.87 * sin(2 * b) - 7.53d * cos(b) - 1.5d * sin(b);
	}

	@Override
	public Date sunrise(final Coordinate coordinate, final Date date) {
		final Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);

		final int day = calendar.get(Calendar.DAY_OF_YEAR);

		final double total = length(coordinate.getLatitude(), day);

		final int hours = (int) total;
		final int minutes = (int) ((((double) total) - ((double) hours)) * 60d);

		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.add(Calendar.HOUR_OF_DAY, -hours / 2);
		calendar.add(Calendar.MINUTE, -minutes / 2);
		calendar.add(Calendar.MINUTE, (int) localSolarTime(day));

		final TimeZone timeZone = TimeZone.getDefault();

		if (timeZone.inDaylightTime(date)) {
			calendar.add(Calendar.MILLISECOND, timeZone.getDSTSavings());
		}

		return calendar.getTime();
	}

	@Override
	public Date sunset(final Coordinate coordinate, final Date date) {
		final Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);

		final int day = calendar.get(Calendar.DAY_OF_YEAR);

		final double total = length(coordinate.getLatitude(), day);

		final int hours = (int) total;
		final int minutes = (int) ((((double) total) - ((double) hours)) * 60d);

		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.add(Calendar.HOUR_OF_DAY, hours / 2);
		calendar.add(Calendar.MINUTE, minutes / 2);
		calendar.add(Calendar.MINUTE, (int) localSolarTime(day));

		final TimeZone timeZone = TimeZone.getDefault();

		if (timeZone.inDaylightTime(date)) {
			calendar.add(Calendar.MILLISECOND, timeZone.getDSTSavings());
		}

		return calendar.getTime();
	}
}
