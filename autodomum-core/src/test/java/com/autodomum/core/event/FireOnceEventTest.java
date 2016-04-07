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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test of the <code>FireOnceEvent</code> class
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
@RunWith(MockitoJUnitRunner.class)
public class FireOnceEventTest {

	private FireOnceEvent fireOnceEvent1;

	private FireOnceEvent fireOnceEvent1b;

	private FireOnceEvent fireOnceEvent2;

	private FireOnceEvent fireOnceEvent3;

	private FireOnceEvent fireOnceEvent4;

	@Mock
	private Clock clock;

	@Before
	public void before() {
		when(clock.millis()).thenReturn(9000l);

		this.fireOnceEvent1 = new FireOnceEvent(clock, 10000, "Event1");
		this.fireOnceEvent1b = new FireOnceEvent(clock, 10000, "Event1");
		this.fireOnceEvent2 = new FireOnceEvent(clock, 10000, "Event2");
		this.fireOnceEvent3 = new FireOnceEvent(clock, 10001, "Event1");
		this.fireOnceEvent4 = new FireOnceEvent(clock, 10000, null);
	}

	@Test
	public void testHashCode() {
		assertEquals(fireOnceEvent1.hashCode(), fireOnceEvent1b.hashCode());
		assertNotEquals(fireOnceEvent1.hashCode(), fireOnceEvent2.hashCode());
		assertNotEquals(fireOnceEvent1.hashCode(), fireOnceEvent3.hashCode());
		assertNotEquals(fireOnceEvent1.hashCode(), fireOnceEvent4.hashCode());
	}

	@Test
	public void testEqualsObject() {
		assertTrue(fireOnceEvent1.equals(fireOnceEvent1b));
		assertFalse(fireOnceEvent1.equals(fireOnceEvent2));
		assertFalse(fireOnceEvent1.equals(fireOnceEvent3));
		assertFalse(fireOnceEvent1.equals(fireOnceEvent4));
		assertFalse(fireOnceEvent4.equals(fireOnceEvent1));
		assertFalse(fireOnceEvent1.equals(null));
		assertFalse(fireOnceEvent1.equals(new Object()));
	}

	@Test
	public void testGetTimestamp() {
		assertEquals(10000, fireOnceEvent1.getTimestamp());
	}

	@Test
	public void testGetName() {
		assertEquals("Event1", fireOnceEvent1.getName());
	}

	@Test
	public void testGetDelay() {
		assertEquals(1000, fireOnceEvent1.getDelay(TimeUnit.MILLISECONDS));
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, this.fireOnceEvent1.compareTo(this.fireOnceEvent1b));
		assertEquals(-1, this.fireOnceEvent1.compareTo(this.fireOnceEvent3));
		assertEquals(1, this.fireOnceEvent3.compareTo(this.fireOnceEvent1));
	}

	@Test
	public void testToString() {
		assertEquals("FireOnceEvent [timestamp=10000, name=Event1]", this.fireOnceEvent1.toString());
	}
}
