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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.autodomum.core.model.Lamp;

/**
 * Test of the <code>LampStateChangedEvent</code> class
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
@RunWith(MockitoJUnitRunner.class)
public class LampStateChangedEventTest {

	private LampStateChangedEvent lampStateChangedEvent1;
	private LampStateChangedEvent lampStateChangedEvent1b;
	private LampStateChangedEvent lampStateChangedEvent2;
	private LampStateChangedEvent lampStateChangedEvent3;

	@Mock
	private Lamp lamp1;

	@Mock
	private Lamp lamp2;

	@Before
	public void before() {
		lampStateChangedEvent1 = new LampStateChangedEvent(lamp1);
		lampStateChangedEvent1b = new LampStateChangedEvent(lamp1);
		lampStateChangedEvent2 = new LampStateChangedEvent(lamp2);
		lampStateChangedEvent3 = new LampStateChangedEvent(null);

		when(lamp1.toString()).thenReturn("Lamp");
	}

	@Test
	public void testHashCode() {
		assertEquals(lampStateChangedEvent1.hashCode(), lampStateChangedEvent1b.hashCode());
		assertNotEquals(lampStateChangedEvent1.hashCode(), lampStateChangedEvent2.hashCode());
		assertNotEquals(lampStateChangedEvent1.hashCode(), lampStateChangedEvent3.hashCode());
	}

	@Test
	public void testEqualsObject() {
		assertTrue(lampStateChangedEvent1.equals(lampStateChangedEvent1));
		assertTrue(lampStateChangedEvent1.equals(lampStateChangedEvent1b));
		assertFalse(lampStateChangedEvent1.equals(lampStateChangedEvent2));
		assertFalse(lampStateChangedEvent1.equals(null));
		assertFalse(lampStateChangedEvent1.equals(lampStateChangedEvent3));
		assertFalse(lampStateChangedEvent3.equals(lampStateChangedEvent1));
		assertFalse(lampStateChangedEvent1.equals(new Object()));
	}

	@Test
	public void testGetLamp() {
		assertEquals(lamp1, lampStateChangedEvent1.getLamp());
	}

	@Test
	public void testToString() {
		assertEquals("LampStateChangedEvent [lamp=Lamp]", lampStateChangedEvent1.toString());
	}
}
