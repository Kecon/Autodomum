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
package com.autodomum.holiday.sweden;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.autodomum.core.holiday.AscensionDay;
import com.autodomum.core.holiday.BoxingDay26;
import com.autodomum.core.holiday.ChristmasDay;
import com.autodomum.core.holiday.ChristmasEve;
import com.autodomum.core.holiday.EasterMonday;
import com.autodomum.core.holiday.EasterSunday;
import com.autodomum.core.holiday.Epiphany;
import com.autodomum.core.holiday.FirstOfMay;
import com.autodomum.core.holiday.GoodFriday;
import com.autodomum.core.holiday.HolidayCollection;
import com.autodomum.core.holiday.NewYearsDay;
import com.autodomum.core.holiday.NewYearsEve;
import com.autodomum.core.holiday.WhitMonday;
import com.autodomum.core.holiday.WhitsunDay;

/**
 * Implementation of all Swedish holidays
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
@Component
@Scope
public class SwedishHolidays extends HolidayCollection {
	public SwedishHolidays() {
		super(new NationalDayOfSweden(), new SwedishAllSaintsDay(), new AscensionDay(), new BoxingDay26(),
				new ChristmasDay(), new ChristmasEve(), new EasterMonday(), new EasterSunday(), new Epiphany(),
				new FirstOfMay(), new GoodFriday(), new SwedishMidsummerDay(), new SwedishMidsummerEve(),
				new NewYearsEve(), new NewYearsDay(), new WhitsunDay(), new WhitMonday());
	}
}
