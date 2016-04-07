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

import java.security.SecureRandom;
import java.time.Clock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.autodomum.core.dao.LampDao;
import com.autodomum.core.daylight.Coordinate;

/**
 * <p>
 * Context containing parameters and information when running events. All
 * context data should be updated before running events.
 * </p>
 * <p>
 * Attributes may be used for storing temporary data between events.
 * </p>
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
@Component
@Scope
public class EventContext {
	private final Clock clock;

	private boolean daylight = false;

	private final Map<String, Object> attributes = new ConcurrentHashMap<>();

	private final SecureRandom random = new SecureRandom();

	private long nextSunrise;

	private long nextSunset;

	private Coordinate coordinate;

	private LampDao lampDao;

	private int hour;

	private int minute;

	private boolean holiday;

	/**
	 * Constructor with default UTC clock
	 */
	public EventContext() {
		this.clock = Clock.systemUTC();
	}

	/**
	 * Constructor with the option to pass another clock
	 * 
	 * @param clock
	 *            the clock
	 */
	public EventContext(final Clock clock) {
		this.clock = clock;
	}

	/**
	 * @return true if daylight, otherwise false
	 */
	public boolean isDaylight() {
		return daylight;
	}

	/**
	 * @param daylight
	 *            set true if daylight, otherwise false
	 */
	public void setDaylight(boolean daylight) {
		this.daylight = daylight;
	}

	/**
	 * @return the next calculated sunrise
	 */
	public long getNextSunrise() {
		return nextSunrise;
	}

	/**
	 * @param nextSunrise
	 *            the next calculated sunrise
	 */
	public void setNextSunrise(long nextSunrise) {
		this.nextSunrise = nextSunrise;
	}

	/**
	 * @return the next calculated sunset
	 */
	public long getNextSunset() {
		return nextSunset;
	}

	/**
	 * @param nextSunset
	 *            the next calculated sunset
	 */
	public void setNextSunset(long nextSunset) {
		this.nextSunset = nextSunset;
	}

	/**
	 * @return the coordinates where the system is located
	 */
	public Coordinate getCoordinate() {
		return coordinate;
	}

	/**
	 * @param coordinate
	 *            the coordinates where the system is located
	 */
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	/**
	 * @return all attributes. Attributes are available between events
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/**
	 * Add an attribute that may be access by another event.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value. A value of null will remove the attribute
	 */
	public void addAttribute(final String key, final Object value) {
		if (key == null) {
			throw new NullPointerException("Parameter key may not be null");
		}

		if (value == null) {
			this.attributes.remove(key);
		} else {
			this.attributes.put(key, value);
		}
	}

	/**
	 * Get an attribute
	 * 
	 * @param key
	 *            the attribute key
	 * @return the value of the attribute or null if the value not is set
	 */
	public Object getAttribute(final String key) {
		if (key == null) {
			return null;
		}
		return this.attributes.get(key);
	}

	/**
	 * Get an attribute as a string
	 * 
	 * @param key
	 *            the attribute key
	 * @return the attribute representation as a string or null if no value was
	 *         set
	 */
	public String getAttributeAsString(final String key) {
		if (key == null) {
			return null;
		}

		final Object value = this.attributes.get(key);
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			return (String) value;
		}

		return value.toString();
	}

	/**
	 * Remove an attribute
	 * 
	 * @param key
	 *            the key to the attribute to remove
	 */
	public void removeAttribute(String key) {
		this.attributes.remove(key);
	}

	/**
	 * @return a random object
	 */
	public SecureRandom getRandom() {
		return random;
	}

	/**
	 * @return the current time in UTC milliseconds
	 */
	public long now() {
		return clock.millis();
	}

	/**
	 * @return the current hour in the local time zone using a 24h clock
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * @param currentHour
	 *            the current hour in the local time zone using a 24h clock
	 */
	public void setHour(int currentHour) {
		this.hour = currentHour;
	}

	/**
	 * @return the current minute in the local time zone
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * @param currentMinute
	 *            the current minute in the local time zone
	 */
	public void setMinute(int currentMinute) {
		this.minute = currentMinute;
	}

	/**
	 * @return true if is holiday, otherwise false
	 */
	public boolean isHoliday() {
		return holiday;
	}

	/**
	 * @param holiday
	 *            true if holiday, otherwise false
	 */
	public void setHoliday(boolean holiday) {
		this.holiday = holiday;
	}

	/**
	 * @return the lamp DAO
	 */
	public LampDao getLampDao() {
		return lampDao;
	}

	/**
	 * @param lampDao
	 *            the lamp DAO
	 */
	@Autowired
	public void setLampDao(LampDao lampDao) {
		this.lampDao = lampDao;
	}

	@Override
	public String toString() {
		return "EventContext [daylight=" + daylight + ", attributes=" + attributes + ", random=" + random
				+ ", nextSunrise=" + nextSunrise + ", nextSunset=" + nextSunset + ", coordinate=" + coordinate
				+ ", hour=" + hour + ", minute=" + minute + ", holiday=" + holiday + "]";
	}

}
