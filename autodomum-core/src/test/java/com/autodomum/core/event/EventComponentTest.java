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
package com.autodomum.core.event;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.autodomum.core.daylight.Coordinate;
import com.autodomum.core.daylight.Daylight;
import com.autodomum.core.holiday.Holiday;

/**
 * Test of the <code>EventComponent</code> class
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
@RunWith(MockitoJUnitRunner.class)
public class EventComponentTest extends EventComponent {

	@Mock
	private Date now;

	@Mock
	private Calendar calendar;

	@Mock
	private EventContext eventContext;

	@Mock
	private Holiday holiday;

	@Mock
	private Daylight daylight;

	@Mock
	private Clock clock;

	@Override
	public Date getNow() {
		return now;
	}

	@Override
	public Calendar getCalendar() {
		return calendar;
	}

	@Test
	public void testUpdateEventContextPassedTodaysSunset() throws ParseException {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		eventContext = spy(EventContext.class);
		calendar = Calendar.getInstance();
		now = dateFormat.parse("2016-04-05 22:30:05");
		Date tomorrow = dateFormat.parse("2016-04-06 22:30:05");
		when(clock.millis()).thenReturn(now.getTime());

		EventCallback sunriseCallback = spy(EventCallback.class);
		EventCallback sunsetCallback = spy(EventCallback.class);

		this.register(SunriseEvent.class, sunriseCallback);
		this.register(SunsetEvent.class, sunsetCallback);

		Coordinate coordinate = mock(Coordinate.class);

		when(this.eventContext.getCoordinate()).thenReturn(coordinate);
		when(this.eventContext.isDaylight()).thenReturn(true);

		this.setDaylight(daylight);
		this.setHoliday(holiday);
		this.setEventContext(eventContext);

		Date sunriseToday = dateFormat.parse("2016-04-05 06:37:08");
		Date sunsetToday = dateFormat.parse("2016-04-05 18:38:09");
		Date sunriseTomorrow = dateFormat.parse("2016-04-06 06:35:02");
		Date sunsetTomorrow = dateFormat.parse("2016-04-06 18:40:03");

		when(holiday.isHoliday(calendar)).thenReturn(true);
		when(daylight.sunrise(coordinate, now)).thenReturn(sunriseToday);
		when(daylight.sunset(coordinate, now)).thenReturn(sunsetToday);
		when(daylight.sunrise(coordinate, tomorrow)).thenReturn(sunriseTomorrow);
		when(daylight.sunset(coordinate, tomorrow)).thenReturn(sunsetTomorrow);

		this.updateEventContext();

		verify(eventContext, times(1)).setHoliday(true);
		verify(eventContext, times(1)).setHour(22);
		verify(eventContext, times(1)).setMinute(30);
		verify(eventContext, times(1)).setNextSunrise(sunriseTomorrow.getTime());
		verify(eventContext, times(1)).setNextSunset(sunsetTomorrow.getTime());
		verify(sunriseCallback, times(0)).work(any(), any());
		verify(sunsetCallback, times(1)).work(any(), any());
	}

	@Test
	public void testUpdateEventContextPassedTodaysSunrise() throws ParseException {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		eventContext = spy(EventContext.class);
		calendar = Calendar.getInstance();
		now = dateFormat.parse("2016-04-05 08:30:05");
		Date tomorrow = dateFormat.parse("2016-04-06 08:30:05");
		when(clock.millis()).thenReturn(now.getTime());

		Coordinate coordinate = mock(Coordinate.class);

		when(this.eventContext.getCoordinate()).thenReturn(coordinate);
		when(this.eventContext.isDaylight()).thenReturn(false);

		EventCallback sunriseCallback = spy(EventCallback.class);
		EventCallback sunsetCallback = spy(EventCallback.class);

		this.register(SunriseEvent.class, sunriseCallback);
		this.register(SunsetEvent.class, sunsetCallback);

		this.setDaylight(daylight);
		this.setHoliday(holiday);
		this.setEventContext(eventContext);

		Date sunriseToday = dateFormat.parse("2016-04-05 06:37:08");
		Date sunsetToday = dateFormat.parse("2016-04-05 18:38:09");
		Date sunriseTomorrow = dateFormat.parse("2016-04-06 06:35:02");
		Date sunsetTomorrow = dateFormat.parse("2016-04-06 18:40:03");

		when(holiday.isHoliday(calendar)).thenReturn(false);
		when(daylight.sunrise(coordinate, now)).thenReturn(sunriseToday);
		when(daylight.sunset(coordinate, now)).thenReturn(sunsetToday);
		when(daylight.sunrise(coordinate, tomorrow)).thenReturn(sunriseTomorrow);
		when(daylight.sunset(coordinate, tomorrow)).thenReturn(sunsetTomorrow);

		this.updateEventContext();

		verify(eventContext, times(1)).setHoliday(false);
		verify(eventContext, times(1)).setHour(8);
		verify(eventContext, times(1)).setMinute(30);
		verify(eventContext, times(1)).setNextSunrise(sunriseTomorrow.getTime());
		verify(eventContext, times(1)).setNextSunset(sunsetToday.getTime());

		verify(sunriseCallback, times(1)).work(any(), any());
		verify(sunsetCallback, times(0)).work(any(), any());
	}

	@Test
	public void testUpdateEventContextBeforeTodaysSunrise() throws ParseException {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		eventContext = spy(EventContext.class);
		calendar = Calendar.getInstance();
		now = dateFormat.parse("2016-04-05 04:30:05");
		Date tomorrow = dateFormat.parse("2016-04-06 04:30:05");
		when(clock.millis()).thenReturn(now.getTime());

		Coordinate coordinate = mock(Coordinate.class);

		when(this.eventContext.getCoordinate()).thenReturn(coordinate);
		when(this.eventContext.isDaylight()).thenReturn(false);

		EventCallback sunriseCallback = spy(EventCallback.class);
		EventCallback sunsetCallback = spy(EventCallback.class);

		this.register(SunriseEvent.class, sunriseCallback);
		this.register(SunsetEvent.class, sunsetCallback);

		this.setDaylight(daylight);
		this.setHoliday(holiday);
		this.setEventContext(eventContext);

		Date sunriseToday = dateFormat.parse("2016-04-05 06:37:08");
		Date sunsetToday = dateFormat.parse("2016-04-05 18:38:09");
		Date sunriseTomorrow = dateFormat.parse("2016-04-06 06:35:02");
		Date sunsetTomorrow = dateFormat.parse("2016-04-06 18:40:03");

		when(holiday.isHoliday(calendar)).thenReturn(false);
		when(daylight.sunrise(coordinate, now)).thenReturn(sunriseToday);
		when(daylight.sunset(coordinate, now)).thenReturn(sunsetToday);
		when(daylight.sunrise(coordinate, tomorrow)).thenReturn(sunriseTomorrow);
		when(daylight.sunset(coordinate, tomorrow)).thenReturn(sunsetTomorrow);

		this.updateEventContext();

		verify(eventContext, times(1)).setHoliday(false);
		verify(eventContext, times(1)).setHour(4);
		verify(eventContext, times(1)).setMinute(30);
		verify(eventContext, times(1)).setNextSunrise(sunriseToday.getTime());
		verify(eventContext, times(1)).setNextSunset(sunsetToday.getTime());

		verify(sunriseCallback, times(0)).work(any(), any());
		verify(sunsetCallback, times(0)).work(any(), any());
	}

	@Test(expected = NullPointerException.class)
	public void testRegisterNullEventType() {
		this.register(null, (a, b) -> {
		});
	}

	@Test(expected = NullPointerException.class)
	public void testRegisterNullCallback() {
		this.register(Event.class, null);
	}

	@Test(expected = NullPointerException.class)
	public void testPublishNull() {
		this.publish(null);
	}

	@Test
	public void testUnregister() throws InterruptedException {
		final EventCallback eventCallback1 = spy(EventCallback.class);
		when(clock.millis()).thenReturn(1l);

		this.register(FireOnceEvent.class, eventCallback1);
		this.setEventContext(eventContext);

		final FireOnceEvent fireOnceEvent = new FireOnceEvent(clock, 1, "value1");

		this.addDelayedEvent(fireOnceEvent);
		this.handleDelayedEvent();

		verify(eventCallback1, times(1)).work(eventContext, fireOnceEvent);

		this.addDelayedEvent(fireOnceEvent);
		this.unregister(FireOnceEvent.class, eventCallback1);
		this.handleDelayedEvent();

		verify(eventCallback1, times(1)).work(eventContext, fireOnceEvent);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRegisterEventOnceTomorrowLowHour() throws ParseException {
		this.registerEventOnceTomorrow(-1, 12, "name1");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRegisterEventOnceTomorrowHighHour() throws ParseException {
		this.registerEventOnceTomorrow(24, 12, "name1");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRegisterEventOnceTomorrowLowMinute() throws ParseException {
		this.registerEventOnceTomorrow(0, -1, "name1");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRegisterEventOnceTomorrowHighMinute() throws ParseException {
		this.registerEventOnceTomorrow(0, 60, "name1");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRegisterEventOnceTomorrowNameNull() throws ParseException {
		this.registerEventOnceTomorrow(0, 59, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRegisterEventOnceTomorrowNameEmpty() throws ParseException {
		this.registerEventOnceTomorrow(0, 59, "");
	}

	@Test
	public void testRegisterEventOnceTomorrow() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		this.calendar = Calendar.getInstance();
		this.calendar.setTime(dateFormat.parse("2016-04-04 22:59:12"));
		this.registerEventOnceTomorrow(10, 12, "name1");
		when(clock.millis()).thenReturn(this.calendar.getTimeInMillis());

		FireOnceEvent fireOnceEvent = (FireOnceEvent) this.delayedEvents.element();
		assertEquals(dateFormat.parse("2016-04-05 10:12:00").getTime(), fireOnceEvent.getTimestamp());
		assertEquals("name1", fireOnceEvent.getName());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRegisterEventOnceNegativeTime() {
		this.registerEventOnce(-1, "name1");
	}

	@Test
	public void testRegisterEventOnce() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		this.now = dateFormat.parse("2016-04-04 22:59:12");
		when(clock.millis()).thenReturn(now.getTime());
		this.setClock(clock);

		this.registerEventOnce(10000, "name1");

		FireOnceEvent fireOnceEvent = (FireOnceEvent) this.delayedEvents.element();
		assertEquals(dateFormat.parse("2016-04-04 22:59:22").getTime(), fireOnceEvent.getTimestamp());
		assertEquals("name1", fireOnceEvent.getName());
	}

	@Test
	public void testAddDelayedEventAndHandleDelayedEvent() throws InterruptedException {
		final EventCallback eventCallback1 = spy(EventCallback.class);
		when(clock.millis()).thenReturn(1l);

		this.register(FireOnceEvent.class, eventCallback1);
		this.setEventContext(eventContext);

		final FireOnceEvent fireOnceEvent = new FireOnceEvent(clock, 1, "value1");

		this.addDelayedEvent(fireOnceEvent);
		this.handleDelayedEvent();

		verify(eventCallback1, times(1)).work(eventContext, fireOnceEvent);

		this.handleDelayedEvent();
		verify(eventCallback1, times(1)).work(eventContext, fireOnceEvent);
	}

	@Test(expected = NullPointerException.class)
	public void testAddDelayedNull() {
		this.addDelayedEvent(null);
	}
}
