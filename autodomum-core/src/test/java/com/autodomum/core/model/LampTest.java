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
package com.autodomum.core.model;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.autodomum.core.model.Lamp;

/**
 * Test of the <code>Lamp</code> class.
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
public class LampTest {

	private Lamp lamp;

	@Before
	public void before() {
		this.lamp = new Lamp();
	}

	@Test
	public void testId() {
		lamp.setId("id1");
		assertEquals("id1", lamp.getId());
		lamp.setId(null);
		assertNull(lamp.getId());
	}

	@Test
	public void testGetName() {
		lamp.setName("id1");
		assertEquals("id1", lamp.getName());
		lamp.setName(null);
		assertNull(lamp.getName());
	}

	@Test
	public void testGetOn() {
		lamp.setOn(true);
		assertTrue(lamp.getOn());
		lamp.setOn(null);
		assertNull(lamp.getOn());

	}

	@Test
	public void testGetX() {
		lamp.setX(1337);
		assertEquals((Integer) 1337, lamp.getX());
		lamp.setX(null);
		assertNull(lamp.getX());
	}

	@Test
	public void testGetY() {
		lamp.setY(1337);
		assertEquals((Integer) 1337, lamp.getY());
		lamp.setY(null);
		assertNull(lamp.getY());
	}

	@Test
	public void testGetCallIds() {
		lamp.setCallIds(Arrays.asList("a", "b", "c"));
		assertEquals(Arrays.asList("a", "b", "c"), lamp.getCallIds());
		lamp.setCallIds(null);
		assertNull(lamp.getCallIds());

	}

	@Test
	public void testClone() {
		this.lamp.setId("id1");
		this.lamp.setName("name1");
		this.lamp.setCallIds(Arrays.asList("a", "b", "c"));
		this.lamp.setOn(true);
		this.lamp.setX(123);
		this.lamp.setY(321);

		Lamp lamp = (Lamp) this.lamp.clone();

		assertEquals("id1", lamp.getId());
		assertEquals("name1", lamp.getName());
		assertEquals(Arrays.asList("a", "b", "c"), lamp.getCallIds());
		assertEquals(true, lamp.getOn());
		assertEquals((Integer) 123, lamp.getX());
		assertEquals((Integer) 321, lamp.getY());
	}

	@Test
	public void testCloneNoCallIds() {
		this.lamp.setId("id1");
		this.lamp.setName("name1");
		this.lamp.setOn(true);
		this.lamp.setX(123);
		this.lamp.setY(321);

		Lamp lamp = (Lamp) this.lamp.clone();

		assertEquals("id1", lamp.getId());
		assertEquals("name1", lamp.getName());
		assertNull(lamp.getCallIds());
		assertEquals(true, lamp.getOn());
		assertEquals((Integer) 123, lamp.getX());
		assertEquals((Integer) 321, lamp.getY());
	}

	@Test
	public void testToString() {
		this.lamp.setId("id1");
		this.lamp.setName("name1");
		this.lamp.setCallIds(Arrays.asList("a", "b", "c"));
		this.lamp.setOn(true);
		this.lamp.setX(123);
		this.lamp.setY(321);

		assertEquals("Lamp [id=id1, name=name1, on=true, x=123, y=321, callIds=[a, b, c]]", this.lamp.toString());
	}
}
