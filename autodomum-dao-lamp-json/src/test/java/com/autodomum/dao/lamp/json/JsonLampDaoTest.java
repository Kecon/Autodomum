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
package com.autodomum.dao.lamp.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.autodomum.core.event.EventComponent;
import com.autodomum.core.event.LampStateChangedEvent;
import com.autodomum.core.model.Lamp;
import com.autodomum.dao.lamp.json.JsonLampDao;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Test of the <code>JsonLampDao</code> class
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonLampDaoTest {

	private JsonLampDao dao;

	@Spy
	private EventComponent eventComponent;

	@Before
	public void before() throws JsonParseException, JsonMappingException, IOException {
		this.dao = new JsonLampDao();
		this.dao.setEventComponent(eventComponent);

		String data = "[{\"id\":\"id1\", \"name\":\"name1\",\"on\":false,\"x\":1,\"y\":2,\"callIds\":[\"a\",\"b\"]}]";
		InputStream inputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
		this.dao.load(inputStream);
	}

	@Test
	public void testGetLamp() {
		Lamp lamp1 = this.dao.getLamp("id1");
		assertEquals("id1", lamp1.getId());
		assertEquals("name1", lamp1.getName());
	}

	@Test
	public void testUpdateLampX() {
		Lamp lamp = new Lamp();
		lamp.setX(3);

		assertEquals((Integer) 1, this.dao.getLamp("id1").getX());

		this.dao.updateLamp("id1", lamp);
		Lamp newLamp = this.dao.getLamp("id1");

		assertEquals((Integer) 3, newLamp.getX());

		verify(eventComponent, times(1)).publish(new LampStateChangedEvent(newLamp));
	}

	@Test
	public void testUpdateLampXNotModified() {
		Lamp lamp = new Lamp();
		lamp.setX(1);

		assertEquals((Integer) 1, this.dao.getLamp("id1").getX());

		this.dao.updateLamp("id1", lamp);
		Lamp newLamp = this.dao.getLamp("id1");

		assertEquals((Integer) 1, newLamp.getX());

		verify(eventComponent, times(1)).publish(new LampStateChangedEvent(newLamp));
	}

	@Test
	public void testUpdateLampY() {
		Lamp lamp = new Lamp();
		lamp.setY(4);

		assertEquals((Integer) 2, this.dao.getLamp("id1").getY());

		this.dao.updateLamp("id1", lamp);
		Lamp newLamp = this.dao.getLamp("id1");

		assertEquals((Integer) 4, newLamp.getY());
		verify(eventComponent, times(1)).publish(new LampStateChangedEvent(newLamp));
	}

	@Test
	public void testUpdateLampYNotModified() {
		Lamp lamp = new Lamp();
		lamp.setY(2);

		assertEquals((Integer) 2, this.dao.getLamp("id1").getY());

		this.dao.updateLamp("id1", lamp);
		Lamp newLamp = this.dao.getLamp("id1");

		assertEquals((Integer) 2, newLamp.getY());
		verify(eventComponent, times(1)).publish(new LampStateChangedEvent(newLamp));
	}

	@Test
	public void testUpdateLampOn() {
		Lamp lamp = new Lamp();
		lamp.setOn(true);

		assertFalse(this.dao.getLamp("id1").getOn());

		this.dao.updateLamp("id1", lamp);
		Lamp newLamp = this.dao.getLamp("id1");

		assertTrue(newLamp.getOn());
		verify(eventComponent, times(1)).publish(new LampStateChangedEvent(newLamp));
	}

	@Test
	public void testUpdateLampOnNotModified() {
		Lamp lamp = new Lamp();
		lamp.setOn(false);

		assertFalse(this.dao.getLamp("id1").getOn());

		this.dao.updateLamp("id1", lamp);
		Lamp newLamp = this.dao.getLamp("id1");

		assertFalse(newLamp.getOn());
		verify(eventComponent, times(1)).publish(new LampStateChangedEvent(newLamp));
	}

	@Test
	public void testUpdateLampInvalidId() {
		Lamp lamp = new Lamp();
		lamp.setOn(false);

		this.dao.updateLamp("id2", lamp);

		verify(eventComponent, times(0)).publish(new LampStateChangedEvent(any()));
	}

	@Test
	public void testGetLamps() {
		Lamp[] lamps = this.dao.getLamps();

		assertEquals(1, lamps.length);
		assertEquals("id1", lamps[0].getId());
		assertEquals("name1", lamps[0].getName());
		assertEquals((Integer) 1, lamps[0].getX());
		assertEquals((Integer) 2, lamps[0].getY());
		assertFalse(lamps[0].getOn());
		assertEquals(Arrays.asList("a", "b"), lamps[0].getCallIds());
	}

	@Test(expected = NullPointerException.class)
	public void testGetLampIdNull() {
		assertNull(this.dao.getLamp(null));
	}

	@Test
	public void testGetLampIdInvalid() {
		assertNull(this.dao.getLamp("id2"));
	}

}
