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

import java.util.Calendar;

/**
 * Calculation of Easter Sunday, based on calculations from
 * <a href="http://en.wikipedia.org/wiki/Computus">Meeus/Jones/Butcher
 * algorithm</a>
 * 
 * @author Kenny Colliander Nordin
 */
public class EasterSunday implements Holiday {

	@Override
	public boolean isHoliday(Calendar calendar) {
		int a = calendar.get(Calendar.YEAR) % 19;
		int b = calendar.get(Calendar.YEAR) / 100;
		int c = calendar.get(Calendar.YEAR) % 100;
		int d = b / 4;
		int e = b % 4;
		int f = (b + 8) / 25;
		int g = (b - f + 1) / 3;
		int h = (19 * a + b - d - g + 15) % 30;
		int i = c / 4;
		int k = c % 4;
		int l = (32 + 2 * e + 2 * i - h - k) % 7;
		int m = (a + 11 * h + 22 * l) / 451;

		int easterMonth = (h + l - 7 * m + 114) / 31;
		--easterMonth;

		int p = (h + l - 7 * m + 114) % 31;

		int easterDay = p + 1;

		return calendar.get(Calendar.MONTH) == easterMonth && calendar.get(Calendar.DAY_OF_MONTH) == easterDay;
	}

}
