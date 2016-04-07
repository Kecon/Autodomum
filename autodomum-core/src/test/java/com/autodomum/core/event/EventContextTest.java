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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.SecureRandom;
import java.time.Clock;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.autodomum.core.dao.LampDao;
import com.autodomum.core.daylight.Coordinate;

/**
 * Test of the <code>EventContext</code> class
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
@RunWith(MockitoJUnitRunner.class)
public class EventContextTest {

	private EventContext eventContext;

	@Mock
	private Clock clock;

	@Before
	public void before() {
		this.eventContext = new EventContext(clock);
	}

	@Test
	public void testDaylight() {
		eventContext.setDaylight(true);
		assertTrue(eventContext.isDaylight());
		eventContext.setDaylight(false);
		assertFalse(eventContext.isDaylight());
	}

	@Test
	public void testNextSunrise() {
		eventContext.setNextSunrise(1234);
		assertEquals(1234, eventContext.getNextSunrise());
		eventContext.setNextSunrise(0);
		assertEquals(0, eventContext.getNextSunrise());
	}

	@Test
	public void testGetNextSunset() {
		eventContext.setNextSunset(1234);
		assertEquals(1234, eventContext.getNextSunset());
		eventContext.setNextSunset(0);
		assertEquals(0, eventContext.getNextSunset());
	}

	@Test
	public void testCoordinate() {
		final Coordinate value = new Coordinate(123, 321);

		eventContext.setCoordinate(value);
		assertEquals(value, eventContext.getCoordinate());
		eventContext.setCoordinate(null);
		assertNull(eventContext.getCoordinate());
	}

	@Test(expected = NullPointerException.class)
	public void testAddAttributeNull() {
		eventContext.addAttribute(null, "value1");
	}

	@Test
	public void testAttributes() {
		eventContext.addAttribute("key1", "value1");
		assertEquals("value1", eventContext.getAttribute("key1"));
		assertNull(eventContext.getAttribute("key2"));
		eventContext.removeAttribute("key1");
		assertNull(eventContext.getAttribute("key1"));
	}

	@Test
	public void testAttributesNullKey() {
		assertNull(eventContext.getAttribute(null));
	}

	@Test
	public void testGetAttributesAsMap() {
		eventContext.addAttribute("key1", "value1");

		assertEquals(Collections.singletonMap("key1", "value1"), eventContext.getAttributes());
	}

	@Test
	public void testGetAttributeAsString() {
		eventContext.addAttribute("key1", "value1");
		eventContext.addAttribute("key2", 1000);
		assertEquals("value1", eventContext.getAttributeAsString("key1"));
		assertEquals("1000", eventContext.getAttributeAsString("key2"));
		assertNull(eventContext.getAttributeAsString(null));
		assertNull(eventContext.getAttributeAsString("key3"));
	}

	@Test
	public void testGetRandom() {
		assertNotNull(eventContext.getRandom());
		assertTrue(eventContext.getRandom() instanceof SecureRandom);
	}

	@Test
	public void testNow() {
		when(clock.millis()).thenReturn(1337l);
		assertEquals(1337l, eventContext.now());
	}

	@Test
	public void testHour() {
		eventContext.setHour(12);
		assertEquals(12, eventContext.getHour());
		eventContext.setHour(0);
		assertEquals(0, eventContext.getHour());
	}

	@Test
	public void testMinute() {
		eventContext.setMinute(12);
		assertEquals(12, eventContext.getMinute());
		eventContext.setMinute(0);
		assertEquals(0, eventContext.getMinute());
	}

	@Test
	public void testHoliday() {
		eventContext.setHoliday(true);
		assertTrue(eventContext.isHoliday());
		eventContext.setHoliday(false);
		assertFalse(eventContext.isHoliday());
	}

	@Test
	public void testLampDao() {
		final LampDao lampDao = mock(LampDao.class);

		eventContext.setLampDao(lampDao);
		assertSame(lampDao, eventContext.getLampDao());
	}
}
